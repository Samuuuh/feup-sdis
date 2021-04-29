import java.io.IOException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

import etc.*;
import node.*;

public class Main implements Services {
    public static void main(String[] args) throws IOException {
        if (args.length > 4) {
            System.out.println("Usage:\n java Main <machineIp> <machinePort> [<chordNodeIp> <chordNodePort>]");
            return;
        }

        Chord node;
        if (args.length == 4) {
            node = new Chord(args[0], args[1]);
            node.join(args[2], args[3]);
        } else if (args.length == 2) {
            node = new Chord(args[0], args[1]);
        } else {
            return;
        }

        Main main = new Main();
        Services stub = (Services) UnicastRemoteObject.exportObject(main, 0);
        try {
            Registry registry = LocateRegistry.getRegistry(Singleton.REGISTER_PORT);
            registry.rebind(Integer.toString(node.id), stub);
        } catch (Exception e) {
            Logger.ANY("Main", "Registry does not exist. Creating a new one...");
            Registry registry = LocateRegistry.createRegistry(Singleton.REGISTER_PORT);
            registry.rebind(Integer.toString(node.id), stub);
        }
    }

    @Override
    public void lookup() {
        // TODO
    } 
}