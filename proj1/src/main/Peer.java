package main;

import channel.*;
import processing.CreateChunk;

import java.io.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class Peer implements Services {
    public static String mc_addr;
    public static int mc_port;

    public static String mdb_addr;
    public static int mdb_port;

    public static String mdr_addr;
    public static int mdr_port;

    public static String version;
    public static String peer_no;

    public static void initChannel(String mcast_addr, int mcast_port, String mdb_addr, int mdb_port, String mdr_addr, int mdr_port) throws IOException {
        new MCChannel(mcast_port, mcast_addr).start();
        new MDBChannel(mdb_port, mdb_addr).start();
        //new MDRChannel(mdr_port, mdr_addr).start();
    }

    public static void main(String[] args) throws IOException {

        if (args.length != 8) {
            System.out.println("Usage:\n java Peer version peer_no mcast_addr mcast_port mdb_addr mdb_port mdr_addr mdr_port");
            return;
        }

        version = args[0];
        peer_no = args[1];

        mc_addr = args[2];
        mc_port = Integer.parseInt(args[3]);

        mdb_addr = args[4];
        mdb_port = Integer.parseInt(args[5]);

        mdr_addr = args[6];
        mdr_port = Integer.parseInt(args[7]);

        initChannel(mc_addr, mc_port, mdb_addr, mdb_port, mdr_addr, mdr_port);

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

        new CreateChunk(filePath, String.valueOf(replicationDeg)).start();

        return "Backup has ended";
    }
}