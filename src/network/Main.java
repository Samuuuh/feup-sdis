package network;

import java.io.IOException;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import network.node.InfoNode;
import network.services.Services;
import network.etc.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ThreadPoolExecutor;


public class Main implements Services {
    private static String ip;
    private static int port;
    public static ChordNode chordNode;
    public static ThreadPoolExecutor threadPool;
    public static ScheduledExecutorService schedulerPool;

    public static void main(String[] args) throws IOException {
        initThreadPool();
        parseParameters(args);

        Main main = new Main();
        Services stub = (Services) UnicastRemoteObject.exportObject(main, 0);
        initRMI(stub);
    }


    public static void parseParameters(String[] args)  {
        if (args.length > 4) {
            System.out.println("Usage:\n java network.Main <machineIp> <machinePort> [<chordNodeIp> <chordNodePort>]");
            System.exit(1);
        }

        ip = args[0];
        port = Integer.parseInt(args[1]);

        if (args.length == 4) {
            InfoNode infoNode = new InfoNode(ip, port);
            InfoNode randomNode = new InfoNode(args[2], Integer.parseInt(args[3]));
            chordNode = new ChordNode(infoNode,randomNode);
        } else if (args.length == 2) {
            InfoNode infoNode = new InfoNode(ip, port);
            chordNode = new ChordNode(infoNode);
        } else System.exit(1);
    }

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

    public static void initThreadPool(){
        threadPool = (ThreadPoolExecutor) Executors.newScheduledThreadPool(Singleton.THREAD_SIZE);
        schedulerPool = Executors.newScheduledThreadPool(Singleton.SCHED_SIZE);
    }

    @Override
    public String backup(String filePath, int repDeg) {
        //new Backup(ip, server.getPort(), filePath, repDeg).request();
        return "Start Lookup";
    }
}