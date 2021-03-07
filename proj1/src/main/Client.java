package main;

import java.io.*;
import java.net.*;
import java.util.*;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
 
public class Client {
    private int peerAccessPoint;
    private String operation;
    private Registry registry;

    public static void main(String[] args) throws IOException {
       if (args.length < 2) {
             System.out.println("Usage:\n java Client <peer_ap> <sub_protocol> <opnd_1> <opnd_2>\n" +
                                "Where <sub_protocol can be one of the following: BACKUP RESTORE DELETE RECLAIM STATE");
             return;
        }

        Client client = new Client(args);
    }

    private Client(String[] args) {
        this.peerAccessPoint = Integer.parseInt(args[0]);
        this.operation = args[1];
        try {
            this.registry = LocateRegistry.getRegistry(this.peerAccessPoint);
        } catch (RemoteException e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

        if(this.operation.equals("BACKUP")) {
            if (args.length != 4) {
                System.out.println("Usage:\n java Client <peer_ap> BACKUP <file> <replication_degree>\n");
                return;
            }

            String file = args[2];
            String replication_degree = args[3];
            try {
                backup(file, replication_degree);
            } catch (RemoteException | NotBoundException e) {
                System.err.println("Client exception: " + e.toString());
                e.printStackTrace();
            }
            
        } else if (this.operation.equals("RESTORE")) {
            if (args.length != 3) {
                System.out.println("Usage:\n java Client <peer_ap> RESTORE <file>\n");
                return;
            }

            String file = args[2];
            restore(file);
        } else if (this.operation.equals("DELETE")) {
            if (args.length != 3) {
                System.out.println("Usage:\n java Client <peer_ap> DELETE <file>\n");
                return;
            }

            String file = args[2];
            delete(file);
        } else if (this.operation.equals("RECLAIM")) {
            if (args.length != 3) {
                System.out.println("Usage:\n java Client <peer_ap> RECLAIM <file>\n");
                return;
            }

            String file = args[2];
            reclaim(file);
        } else if (this.operation.equals("STATE")) {
            if (args.length != 2) {
                System.out.println("Usage:\n java Client <peer_ap> STATE\n");
                return;
            }

            state();
        } else {
            System.out.println("Usage:\n java Client <peer_ap> <sub_protocol> <opnd_1> <opnd_2>\n" +
                                "Where <sub_protocol can be one of the following: BACKUP RESTORE DELETE RECLAIM STATE");  
        }
    }

    private void backup(String file, String replication_degree) throws RemoteException, NotBoundException {
        Services stub = (Services) this.registry.lookup("Services");
        String response = stub.backup();
    }   

    private void restore(String file) {
        System.out.println("restore");
    }  

    private void delete(String file) {
        System.out.println("delete");
    }

    private void reclaim(String file) {
        System.out.println("reclaim");
    }  

    private void state() {
        System.out.println("state");
    }      
}