package main;

import channel.BackupChannel;
import subProtocol.BackupSubProtocol;
import file.Chunk;
import file.FileHandler;

import java.io.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;


public class Peer implements Services {

    public static int mcast_port;
    public static String mcast_addr;
    public static int port;


    public static void initChannel(int mcast_port, String mcast_addr) throws IOException {
        new BackupChannel(mcast_port, mcast_addr).start();
    }

    public static void main(String[] args) throws IOException {

        // TODO: generates ID.
        if (args.length != 3) {
            System.out.println("Usage:\n java Peer mcast_port mcast_addr port");
            return;
        }

        mcast_port = Integer.parseInt(args[0]);
        mcast_addr = args[1];
        port = Integer.parseInt(args[2]);

        initChannel(mcast_port, mcast_addr);


        // Bind Services. 
        try {
            Peer obj = new Peer();
            Services stub = (Services) UnicastRemoteObject.exportObject(obj, 0);

            // TODO: perguntar ao professor qual vai ser esta porta?
            Registry registry = LocateRegistry.createRegistry(1888);
            registry.rebind("Services", stub);
        } catch (Exception e) {
            System.out.println("ERROR: Error while trying to bind stub");
            e.printStackTrace();
        }

        System.out.println("Server is running");

    }

    public String backup(String filePath, int replicationDeg) throws IOException {
        // TODO: to delete.
        System.out.println("Backup called");

        try {
            byte[] fileContent = FileHandler.readFile(filePath);
            Chunk[] chunks = FileHandler.splitFile(fileContent);

            new BackupSubProtocol(filePath, "fileId", "senderId", replicationDeg, chunks).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Backup has ended";
    }


}