package main;

// Java Packages

import channel.MCChannel;
import channel.MDBChannel;
import channel.MDRChannel;
import channel.TCPChannel;
import main.etc.Logger;
import main.etc.Singleton;
import process.request.*;
import send.Send;
import state.SaveState;
import state.State;
import tasks.Tasks;

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

    // TCP
    public static int tcp_port;

    // Chunks that are being restored.
    public static Tasks restoreTasks = new Tasks();
    // Removed chunks from reclaim that will need to be restored.
    public static Tasks reclaimBackupTasks = new Tasks();

    // The store will be canceled if chunk achieve the replication degree.
    public static Tasks storeTasks = new Tasks();

    public static void initChannel(String mcast_addr, int mcast_port, String mdb_addr, int mdb_port, String mdr_addr, int mdr_port) throws IOException {
        new MCChannel(mcast_port, mcast_addr).start();
        new MDBChannel(mdb_port, mdb_addr).start();
        new MDRChannel(mdr_port, mdr_addr).start();
        new TCPChannel().start();
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

        sendBoot();
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
    // Send message saying that the peer is on.
    public static void sendBoot(){

        if (version.equals(Singleton.VERSION_ENH)) {
            new Send(Singleton.BOOT, Peer.mc_addr, Peer.mc_port).start();
            Logger.REQUEST("Peer", "BOOT requested");
        }
    }
    public String backup(String filePath, int replicationDeg)  {
        Logger.REQUEST("Peer", "BACKUP requested");
        new RequestFilePutChunk(filePath, String.valueOf(replicationDeg)).start();
        return "Backup started";
    }

    public String restore(String fileName)  {
        Logger.ANY("Peer", "RESTORE requested");
        new RequestGetChunk(fileName).start();
        return "Store has executed";
    }

    public String delete(String filename)  {
        Logger.ANY("Peer", "DELETE requested");

        new RequestDelete(filename).start();

        return "Delete has executed";
    }

    public String reclaim(String space)  {
        Logger.ANY("Peer", "RECLAIM requested");

        // Process reclaim.
        new RequestReclaim(space).start();

        return "Reclaim has executed";
    }

    public State state(){
        return peer_state;
    }



}