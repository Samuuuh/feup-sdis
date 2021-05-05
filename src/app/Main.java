package app;

import java.io.IOException;
import java.net.InetAddress;
import java.rmi.server.UnicastRemoteObject;
import service.node.*;
import etc.*;
import service.server.com.Send;
import service.server.com.TCPChannel;

public class Main implements Services {
    private static Chord node;
    public static void main(String[] args) throws IOException {

        parseParameters(args);

        Main main = new Main();
        Services stub = (Services) UnicastRemoteObject.exportObject(main, 0);
        try {
            // Registry registry = LocateRegistry.getRegistry(Singleton.REGISTER_PORT);
            // registry.rebind(Integer.toString(service.node.id), stub);
            new TCPChannel(Integer.parseInt(args[1])).start();

            InetAddress ipSend = InetAddress.getByName(args[2]);
            new Send(Integer.parseInt(args[3])).start();

        } catch (Exception e) {
            /*Logger.ANY("App.Main", "Registry does not exist. Creating a new one...");
            Registry registry = LocateRegistry.createRegistry(Singleton.REGISTER_PORT);
            registry.rebind(Integer.toString(service.node.id), stub);*/
        }
    }

    @Override
    public void lookup() {
        // TODO
    }

    public static void parseParameters(String[] args){
        if (args.length > 4) {
            System.out.println("Usage:\n java App.Main <machineIp> <machinePort> [<chordNodeIp> <chordNodePort>]");
            System.exit(1);
        }

        if (args.length == 4) {
            node = new Chord(args[0], args[1]);
            node.join(args[2], args[3]);
        } else if (args.length == 2) {
            node = new Chord(args[0], args[1]);
        } else  System.exit(1);

    }
}