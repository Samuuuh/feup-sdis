package app;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.server.UnicastRemoteObject;

import service.Server;
import service.node.*;
import service.etc.*;

import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

public class Main implements Services {
    private static Chord node;
    private static String ip;
    private static String port;
    private static Server server;

    public static void main(String[] args) throws IOException {
        parseParameters(args);

        Main main = new Main();
        Services stub = (Services) UnicastRemoteObject.exportObject(main, 0);
        initNetworkServer();
        // Initialize the RMI.
        try {
            Registry registry = LocateRegistry.getRegistry(Singleton.REGISTER_PORT);
            registry.rebind(port, stub);
        } catch (Exception e) {
            Logger.ANY("App.Main", "Registry does not exist. Creating a new one...");
            Registry registry = LocateRegistry.createRegistry(Singleton.REGISTER_PORT);
            registry.rebind(port, stub);
        }
    }

    public static void parseParameters(String[] args){
        if (args.length > 4) {
            System.out.println("Usage:\n java App.Main <machineIp> <machinePort> [<chordNodeIp> <chordNodePort>]");
            System.exit(1);
        }

        ip = args[0];
        port = args[1];

        if (args.length == 4) {
            node = new Chord(args[0], args[1]);
            node.join(args[2], args[3]);
        } else if (args.length == 2) {
            node = new Chord(args[0], args[1]);
        } else System.exit(1);
    }

    public static void initNetworkServer() throws IOException {
        server = new Server();
        server.start();
    }

    @Override
    public String backup(String filePath, int repDeg) throws IOException, ClassNotFoundException {
        new Backup(ip, server.getPort(), filePath, repDeg).request();
        return "Start Lookup";
    }
}