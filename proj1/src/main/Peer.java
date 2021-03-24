package main;

// Java Packages

import channel.MCChannel;
import channel.MDBChannel;
import channel.MDRChannel;
import main.etc.Logger;
import main.etc.Singleton;
import process.request.RequestDelete;
import process.request.RequestGetChunk;
import process.request.RequestPutChunk;
import state.SaveState;
import state.State;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

// Custom Packages

public class Peer implements Services {
    // Peer Version
    public static String version;
    public static String peer_no;

    // Peer State
    public static State peer_state;

    // Multicast Address
    public static String mc_addr;
    public static int mc_port;
    public static String mdb_addr;
    public static int mdb_port;
    public static String mdr_addr;
    public static int mdr_port;


    // This is a hashmap that stores the scheduled restores to be done.


    public static void initChannel(String mcast_addr, int mcast_port, String mdb_addr, int mdb_port, String mdr_addr, int mdr_port) throws IOException {
        new MCChannel(mcast_port, mcast_addr).start();
        new MDBChannel(mdb_port, mdb_addr).start();
        new MDRChannel(mdr_port, mdr_addr).start();
    }

    public static void main(String[] args) throws IOException {

        if (args.length != 8) {
            System.out.println("Usage:\n java Peer version peer_no mcast_addr mcast_port mdb_addr mdb_port mdr_addr mdr_port");
            return;
        }

        setVariables(args);
        restoreState();
        initChannel(mc_addr, mc_port, mdb_addr, mdb_port, mdr_addr, mdr_port);

        // Bind Services.
        Peer obj = new Peer();
        Services stub = (Services) UnicastRemoteObject.exportObject(obj, 0);

        try {
            Registry registry = LocateRegistry.getRegistry(Singleton.REGISTER_PORT);
            registry.rebind(peer_no, stub);

        } catch (Exception e) {
            Logger.ANY("Peer", "Registry does not exist. Creating a new one...");
            Registry registry = LocateRegistry.createRegistry(Singleton.REGISTER_PORT);
            registry.rebind(peer_no, stub);
        }
        Logger.ANY("Peer", "Server is running");
    }

    private static void setVariables(String[] args) {
        version = args[0];
        peer_no = args[1];

        mc_addr = args[2];
        mc_port = Integer.parseInt(args[3]);
        mdb_addr = args[4];
        mdb_port = Integer.parseInt(args[5]);
        mdr_addr = args[6];
        mdr_port = Integer.parseInt(args[7]);
    }

    private static void restoreState() {
        try {
            FileInputStream fileIn = new FileInputStream(Singleton.getStatePath(peer_no) + Singleton.STATE_FILE_NAME);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            peer_state = (State) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            Logger.ANY("Peer", "Initializing new state.");
            peer_state = new State(peer_no);
        }
        new SaveState().start();
    }


    // SERVICES

    public String backup(String filePath, int replicationDeg)  {
        Logger.ANY("Peer", "BACKUP requested");
        new RequestPutChunk(filePath, String.valueOf(replicationDeg)).start();

        return "Backup has ended";
    }

    public String restore(String fileName)  {
        Logger.ANY("Peer", "RESTORE requested");
        new RequestGetChunk(fileName).start();

        return "Store has ended";
    }

    public String delete(String filename)  {
        Logger.ANY("Peer", "DELETE requested");

        new RequestDelete(filename).start();

        return "Delete has ended";
    }
}