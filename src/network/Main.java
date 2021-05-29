
package network;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.math.BigInteger;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import network.node.InfoNode;
import network.node.State;
import network.services.backup.SendBackup;
import network.services.reclaim.SendReclaim;
import network.services.delete.SendDelete;
import network.services.restore.SendRestore;
import network.services.Services;
import network.etc.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.*;


public class Main implements Services {
    private static String ip;
    private static int port;
    public static State state;
    public static ChordNode chordNode;
    public static ThreadPoolExecutor threadPool;
    public static ScheduledExecutorService schedulerPool;

    /**
    * This is the main method which starts the peer
    * @param args Should have following arguments: <machineIp> <machinePort> [<chordNodeIp> <chordNodePort>]
    */
    public static void main(String[] args) throws IOException {
        initThreadPool();
        parseParameters(args);

        Main main = new Main();
        Services stub = (Services) UnicastRemoteObject.exportObject(main, 0);
        restoreState();
        saveState();
        initRMI(stub);
    }


    /**
    * Parse the parameters of passed
    * @param args Should have following arguments <machineIp> <machinePort> [<chordNodeIp> <chordNodePort>]
    */
    public static void parseParameters(String[] args) {
        if (args.length > 4) {
            System.out.println("Usage:\n java network.Main <machineIp> <machinePort> [<chordNodeIp> <chordNodePort>]");
            System.exit(1);
        }

        ip = args[0];
        port = Integer.parseInt(args[1]);

        if (args.length == 4) {
            InfoNode infoNode = new InfoNode(ip, port);
            InfoNode randomNode = new InfoNode(args[2], Integer.parseInt(args[3]));
            chordNode = new ChordNode(infoNode, randomNode);
        } else if (args.length == 2) {
            InfoNode infoNode = new InfoNode(ip, port);
            chordNode = new ChordNode(infoNode);

        } else System.exit(1);
    }

    /**
    * Init RMI
    * @param stub Stub with the UnicastRemoteObject
    */
    public static void initRMI(Services stub) throws RemoteException {
        try {
            Registry registry = LocateRegistry.getRegistry(Singleton.REGISTER_PORT);
            registry.rebind(String.valueOf(port), stub);
        } catch (Exception e) {
            Logger.ANY("App.Main", "Registry does not exist. Creating a new one...");
            Registry registry = LocateRegistry.createRegistry(Singleton.REGISTER_PORT);
            registry.rebind(String.valueOf(port), stub);
        }
    }

    /**
    * Init Thread Pools to be used
    */
    public static void initThreadPool() {
        threadPool = (ThreadPoolExecutor) Executors.newScheduledThreadPool(Singleton.THREAD_SIZE);
        schedulerPool = Executors.newScheduledThreadPool(Singleton.SCHED_SIZE);
    }

    /**
    * Get the port of peer
    * @return int Port number
    */
    public static int getPort() {
        return port;
    }

    // -------------------------------- STATE -------------------------------- //
    /**
    * Save the state of peer
    */
    public static void saveState() {
        schedulerPool.scheduleAtFixedRate(new Runnable() {
            @Override
            public void run() {
                try {
                    state.saveState();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }, 0, Singleton.SAVE_PERIOD * 1000L, TimeUnit.MILLISECONDS);
    }

    /**
    * Restore the state of peer
    */
    private static void restoreState() {
        try {
            FileInputStream fileIn = new FileInputStream(Singleton.getStatePath() + Singleton.STATE_FILENAME);
            ObjectInputStream in = new ObjectInputStream(fileIn);
            state = (State) in.readObject();
            in.close();
            fileIn.close();
        } catch (IOException | ClassNotFoundException e) {
            Logger.ANY("Peer", "Initializing new state.");
            state = new State();
        }
    }

    //  -------------------------------- SERVICES -------------------------------- //
    
    /**
    * Backup Service
    * @param filePath file to be backup
    * @param repDeg Desired repdeg of the file
    */
    @Override
    public String backup(String filePath, int repDeg) {
        InfoNode sucessor = chordNode.getSuccessor();
        Main.threadPool.execute(new SendBackup(sucessor.getIp(), sucessor.getPort(), filePath, chordNode.getInfoNode(), repDeg));
        return "Start Backup";
    }

    /**
    * Restore Service
    * @param filePath file to be restored
    */
    @Override
    public String restore(String filePath) {
        InfoNode sucessor = chordNode.getSuccessor();
        Main.threadPool.execute(new SendRestore(sucessor.getIp(), sucessor.getPort(), filePath, chordNode.getInfoNode()));
        return "Start Restore";
    }

    /**
    * Reclaim Service
    * @param targetId target id
    * @param size Size to be reclaimed
    */
    @Override
    public String reclaim(String targetId, int size) {
        Main.threadPool.submit(new SendReclaim(new BigInteger(targetId), size));
        return "Reclaim initiated";
    }

    /**
    * Delete Service
    * @param filePath file to delete 
    */
    @Override
    public String delete(String filePath) {
        Main.threadPool.execute(new SendDelete(filePath, chordNode.getInfoNode()));
        return "Deleting " + filePath;
    }
}