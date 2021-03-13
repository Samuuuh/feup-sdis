package main;

import java.io.*;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.NotBoundException;
 
public class Client {
    private String peerAccessPoint;
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

    private Client(String[] args) throws IOException {
        this.peerAccessPoint = args[0];
        this.operation = args[1];

        try {
            this.registry = LocateRegistry.getRegistry(Definitions.REGISTER_PORT);
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
            boolean isNumber = replication_degree.matches("\\d+");

            if(!isNumber) {
                System.out.println("Replication degree should be an Integer");
                return;
            }
            try {
                System.out.println("Calling backup");
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

    private void backup(String filePath, String replication_degree) throws IOException, NotBoundException {
        Services stub = (Services) this.registry.lookup(this.peerAccessPoint);
        int replication = Integer.parseInt(replication_degree);
        String response = stub.backup(filePath, replication);

        System.out.println(response);
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