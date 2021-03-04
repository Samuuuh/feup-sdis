package build.proj;

import java.io.*;
import java.net.*;
import java.util.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;


public class Peer implements Services {

    public static void main(String[] args) throws IOException { 

        if (args.length != 1) { 
            System.out.println("Usage:\n java Peer port");
            return;
        } 

        Integer port = Integer.parseInt(args[0]);

        // Bind Services. 
        try {
            Peer obj = new Peer();
            Services stub = (Services) UnicastRemoteObject.exportObject(obj, 8081);
            Registry registry = LocateRegistry.createRegistry(port); 
            registry.rebind("Services", stub);    

            System.out.println("Server is running");  
        } catch (Exception e) {
            System.out.println("ERROR: Error while trying to bind stub"); 
            e.printStackTrace();
        }
    }
     
    public Peer() { }

    public String backup() {
        return "Hello World";
    }
}