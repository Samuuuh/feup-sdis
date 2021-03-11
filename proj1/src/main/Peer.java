package main;

import channel.BackupChannel;
import subProtocol.BackupSubProtocol;
import file.Chunk;
import file.FileHandler;

import java.io.*;
import java.rmi.RemoteException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

// TODO: esperar resposta de um ficheiro especifico.
public class Peer implements Services {

    public static int mcast_port;
    public static String mcast_addr;
    public static int port;
    public static String version;
    public static int peer_no;

    public static void initChannel(int mcast_port, String mcast_addr) throws IOException {
        new BackupChannel(mcast_port, mcast_addr).start();
    }

    public static void main(String[] args) throws IOException {

        if (args.length != 4) {
            System.out.println("Usage:\n java Peer version peer_no mcast_port mcast_addr");
            return;
        }

        version = args[0];
        peer_no = Integer.parseInt(args[1]);
        mcast_port = Integer.parseInt(args[2]);
        mcast_addr = args[3];

        initChannel(mcast_port, mcast_addr);
        // Bind Services.

        // TODO: perguntar ao professor qual vai ser esta porta?
        // O peer nao cria o registo. Fazemos um programa java separado. Importante.
        Peer obj = new Peer();
        Services stub = (Services) UnicastRemoteObject.exportObject(obj, 0);

        try {
            Registry registry = LocateRegistry.getRegistry(Definitions.REGISTER_PORT);
            registry.rebind(String.valueOf(peer_no), stub);

        } catch (Exception e) {
            System.out.println("ERROR: Error while trying to bind stub");
            System.err.println("Registry does not exist. Creating a new one...");

            Registry registry = LocateRegistry.createRegistry(Definitions.REGISTER_PORT);
            registry.rebind(String.valueOf(peer_no), stub);
        }

        System.out.println("Server is running");
    }

    public String backup(String filePath, int replicationDeg) throws IOException {
        // TODO: to delete.
        /*
        System.out.println("Backup called");

        try {
            byte[] fileContent = FileHandler.readFile(filePath);
            // Chunk[] chunks = FileHandler.splitFile(fileContent);
            Chunk chunk = new Chunk(0, fileContent);
            Chunk[] chunks = {chunk};
            new BackupSubProtocol(filePath, "fileId", "senderId", replicationDeg, chunks).start();
        } catch (Exception e) {
            e.printStackTrace();
        }

         */

        return "Backup has ended";
    }


}