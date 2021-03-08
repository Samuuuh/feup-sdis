package build.lab3;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

public class Server implements Handler {
    private static DatagramSocket socket = null;

    private static HashMap<String, String> dnsTable = new HashMap<String, String>();

    public Server() {
    }

    public String handleRequest(String request) {

        displayRequest(request);
        String response = processRequest(request);

        return response;

    }

    /**
     * @param args[0] (remote_object_name) - remote object that implements the
     *                remote interface
     */
    public static void main(String[] args) throws IOException {

        if (args.length != 1) {
            System.out.println("Wrong number of arguments, usage :: java Server <remote_object_name>");
            System.exit(-1);
        }

        String remote_object_name = args[0];
        run(remote_object_name);
    }

    /**
     * Initializes the thread process.
     * 
     * @param remote_object_name - Remote object that implements the remote
     *                           interface.
     */
    public static void run(String remote_object_name) {
        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);

        try {
            Server obj = new Server();
            // Exports a remote object stub, so it can receive incoming calls at the
            // specified port.
            Handler stub = (Handler) UnicastRemoteObject.exportObject(obj, 8081);

            Registry registry = LocateRegistry.createRegistry(1888);
            registry.rebind(remote_object_name, stub);
            System.out.println("Server ready");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static String processRequest(String messageString) {
        messageString = messageString.trim();
        final String[] listMessageString = messageString.split("\\s+");

        return switch (listMessageString[0]) {
            case "LOOKUP" -> lookup(listMessageString[1]);
            case "REGISTER" -> register(listMessageString[1], listMessageString[2]);
            default -> "-1";
        };
    }

    // It returns -1, if the name has already been registered, and the number of
    // bindings in the service, otherwise.
    /**
     * 
     * @param dnsName   - URL of the website.
     * @param ipAddress - IP for the given dns.
     * @return "OK" on success, -1 on error (has already been registred).
     */
    private static String register(String dnsName, String ipAddress) {
        if (dnsTable.get(dnsName) == null) {
            dnsTable.put(dnsName, ipAddress);
            return "OK";
        } else {
            return "-1";
        }
    }

    /**
     * Lookup of the hashtable, the ipAddress for a given DNS.
     * 
     * @param dnsName -
     * @return IP address in dotted decimal format previosly bound to a DNS. Return
     *         NOT_FOUND string, if the name was not previously registered
     */
    private static String lookup(String dnsName) {
        if (dnsTable.get(dnsName) == null) {
            return "NOT_FOUND";
        } else {
            return dnsTable.get(dnsName);
        }
    }

    private static void displayRequest(String requestMessage) {
        System.out.println("Server: " + requestMessage);
    }
}