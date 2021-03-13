package main;

import channel.BackupChannel;
import subProtocol.BackupSubProtocol;
import file.Chunk;
import file.FileHandler;

import java.io.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

// TODO: esperar resposta de um ficheiro especifico.
public class Peer implements Services {

    public static int mcast_port;
    public static String mcast_addr;
    public static int port;
    public static String version;
    public static String peer_no;

    public static void initChannel(int mcast_port, String mcast_addr) throws IOException {
        new BackupChannel(mcast_port, mcast_addr).start();
    }

    public static void main(String[] args) throws IOException {

        if (args.length != 4) {
            System.out.println("Usage:\n java Peer version peer_no mcast_port mcast_addr");
            return;
        }

        version = args[0];
        peer_no = args[1];
        mcast_port = Integer.parseInt(args[2]);
        mcast_addr = args[3];

        initChannel(mcast_port, mcast_addr);

        // Bind Services.
        Peer obj = new Peer();
        Services stub = (Services) UnicastRemoteObject.exportObject(obj, 0);

        try {
            Registry registry = LocateRegistry.getRegistry(Definitions.REGISTER_PORT);
            registry.rebind(peer_no, stub);

        } catch (Exception e) {
            System.out.println("ERROR: Error while trying to bind stub");
            System.err.println("Registry does not exist. Creating a new one...");

            Registry registry = LocateRegistry.createRegistry(Definitions.REGISTER_PORT);
            registry.rebind(peer_no, stub);
        }

        System.out.println("Server is running");
    }

    public String backup(String filePath, int replicationDeg) throws IOException {
        System.out.println("Peer\t\t:: backup START!");

        try {
            byte[] fileContent = FileHandler.readFile(filePath);
            Chunk[] chunks = FileHandler.splitFile(fileContent);
            new BackupSubProtocol(filePath, "fileId", peer_no, replicationDeg, chunks).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
        System.out.println("Peer\t\t:: backup END!");
        return "Backup has ended";
    }
}