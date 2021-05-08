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



public class Main implements Services {
    private static String ip;
    private static int port;
    public static ChordNode chordNode;


    public static void main(String[] args) throws IOException {
        parseParameters(args);

        Main main = new Main();
        Services stub = (Services) UnicastRemoteObject.exportObject(main, 0);
        initRMI(stub);
        initChord();
    }


    public static void parseParameters(String[] args) throws UnknownHostException {
        if (args.length > 4) {
            System.out.println("Usage:\n java network.Main <machineIp> <machinePort> [<chordNodeIp> <chordNodePort>]");
            System.exit(1);
        }

        ip = args[0];
        port = Integer.parseInt(args[1]);

        if (args.length == 4) {
            //node = new Chord(args[0], args[1]);
            //node.join(args[2], args[3]);
        } else if (args.length == 2) {
            //node = new Chord(args[0], args[1]);
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

    public static void initChord() {
        String uncodedId = Singleton.getIdUncoded(ip, port);
        BigInteger encodedId = Singleton.encode(uncodedId);
        InfoNode infoNode = new InfoNode(encodedId, port, ip.toString());
        chordNode = new ChordNode(infoNode);
    }

    @Override
    public String backup(String filePath, int repDeg) {
        //new Backup(ip, server.getPort(), filePath, repDeg).request();
        return "Start Lookup";
    }
}