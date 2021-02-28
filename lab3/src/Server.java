package src.hello;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server implements Hello {
    private static DatagramSocket socket = null;
    private static HashMap<String, String> dnsTable = new HashMap<String, String>();
    
    public Server() {}  

    public String sayHello(String request){

        displayRequest(request);
        String response = processRequest(request);

        return response; 

    }
    
    public static void main(String[] args) throws IOException {

        if (args.length < 1) { 
            System.out.println("Wrong number of arguments, usage :: java Server <remote_object_name>");
            System.exit(-1);
        }

        String remote_object_name = args[0];
        run(remote_object_name);
    }

    public static void run(String remote_object_name) {
        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        try {
            Server obj = new Server();
            Hello stub = (Hello) UnicastRemoteObject.exportObject(obj, 0);

            // Bind the remote object's stub in the registry
            //Registry registry = LocateRegistry.getRegistry();
            Registry registry = LocateRegistry.createRegistry(1888);
            registry.bind(remote_object_name, stub);

            System.err.println("Server ready");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String processRequest(String messageString) {
        messageString =  messageString.trim();
        final String[] listMessageString = messageString.split("\\s+");

        return switch (listMessageString[0]) {
            case "LOOKUP" -> lookup(listMessageString[1]);
            case "REGISTER" -> register(listMessageString[1], listMessageString[2]);
            default -> "-1";
        };
    }

    // It returns -1, if the name has already been registered, and the number of bindings in the service, otherwise.
    private static String register(String dnsName, String ipAddress) {
        if(dnsTable.get(dnsName) == null) {
            dnsTable.put(dnsName, ipAddress);
            // Return: number of bindings in the service?
            return "OK";    //TODO: No specification.
        } else {
            return "-1";
        }
    }

    // to retrieve the IP address previously bound to a DNS name. 
    // It returns the IP address in the dotted decimal format or the NOT_FOUND string, if the name was not previously registered.
    private static String lookup(String dnsName) {
        if(dnsTable.get(dnsName) == null) {
            return "NOT_FOUND";
        }
        else {
            return dnsTable.get(dnsName);
        }
    }

    private static void displayRequest(String requestMessage){
        System.out.println("Server: " + requestMessage);
    }
}