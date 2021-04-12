package main;

import main.etc.Singleton;
import state.State;

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
            this.registry = LocateRegistry.getRegistry(Singleton.REGISTER_PORT);
        } catch (RemoteException e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

        if (this.operation.equals("BACKUP")) {
            if (args.length != 4) {
                System.out.println("Usage:\n java Client <peer_ap> BACKUP <file> <replication_degree>\n");
                return;
            }

            String file = args[2];
            String replication_degree = args[3];
            boolean isNumber = replication_degree.matches("\\d+");

            if (!isNumber) {
                System.out.println("Replication degree should be an Integer");
                return;
            }
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
                System.out.println("Usage:\n java Client <peer_ap> RECLAIM <size>\n");
                return;
            }

            String space = args[2];
            reclaim(space);
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

    private void restore(String filePath) {
        try {
            Services stub = (Services) this.registry.lookup(this.peerAccessPoint);
            String response = stub.restore(filePath);
            System.out.println(response);
        } catch (IOException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    private void delete(String filePath) {
        try {
            Services stub = (Services) this.registry.lookup(this.peerAccessPoint);
            String response = stub.delete(filePath);
            System.out.println(response);
        } catch (IOException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    private void reclaim(String space) {
        try {
            Services stub = (Services) this.registry.lookup(this.peerAccessPoint);
            String response = stub.reclaim(space);
            System.out.println(response);
        } catch (IOException | NotBoundException e) {
            e.printStackTrace();
        }
    }

    private void state() {
        try {
            Services stub = (Services) this.registry.lookup(this.peerAccessPoint);
            State state = stub.state();
            System.out.println(state);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}
