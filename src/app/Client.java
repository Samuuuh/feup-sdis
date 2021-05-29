package app;

import network.etc.Logger;
import network.etc.Singleton;
import network.services.Services;

import java.io.IOException;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;


public class Client {
    private String accessPoint;
    private Registry registry;
    private String operation;

    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage:\n java Client <peer_ap> <sub_protocol> <opnd_1> <opnd_2>\n" +
                    "Where <sub_protocol can be one of the following: BACKUP RESTORE DELETE RECLAIM STATE");
            return;
        }

        new Client(args);
    }

    private Client(String[] args) throws IOException {
        accessPoint = args[0];
        operation = args[1];

        try {
            this.registry = LocateRegistry.getRegistry(Singleton.REGISTER_PORT);
        } catch (RemoteException e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

        if (operation.equals("BACKUP")) {
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

            backup(file, replication_degree);

        } else if (this.operation.equals("RESTORE")) {
            if (args.length != 3) {
                System.out.println("Usage:\n java Client <peer_ap> RESTORE <file>\n");
                return;
            }

            String file = args[2];
            restore(file);

        } else if (this.operation.equals("DELETE")) {
            if (args.length != 3) {
                System.out.println("Usage:\n java Client <peer_ap> RESTORE <file>\n");
                return;
            }

            String file = args[2];
            delete(file);

        } else if (this.operation.equals("RECLAIM")) {
            if (args.length != 4){
                System.out.println("Usage:\n java Client <peer_ap> RECLAIM <chordId> <size>\n");
                return;
            }
            String id = args[2];
            Integer size = Integer.parseInt(args[3]);
            reclaim(id, size);
        } else if (this.operation.equals("STATE")) {
            // TODO
        } else {
            System.out.println("Usage:\n java Client <peer_ap> <sub_protocol> <opnd_1> <opnd_2>\n" +
                    "Where <sub_protocol can be one of the following: BACKUP RESTORE DELETE RECLAIM STATE");
        }
    }

    private void backup(String filePath, String replication_degree)  {
        try {
            Services stub = (Services) this.registry.lookup(this.accessPoint);
            int replication = Integer.parseInt(replication_degree);
            String response = stub.backup(filePath, replication);
        }catch (Exception e){
            Logger.ERR(this.getClass().getName(), "Error on requesting backup.");
            e.printStackTrace();
        }
    }

    private void restore(String filePath)  {
        try {
            Services stub = (Services) this.registry.lookup(this.accessPoint);
            String response = stub.restore(filePath);
            System.out.println(response);
        }catch (Exception e){
            Logger.ERR(this.getClass().getName(), "Error on requesting backup.");
            e.printStackTrace();
        }
    }

    private void delete(String filePath)  {
        try {
            Services stub = (Services) this.registry.lookup(this.accessPoint);
            String response = stub.delete(filePath);
            System.out.println(response);
        }catch (Exception e){
            Logger.ERR(this.getClass().getName(), "Error on requesting backup.");
            e.printStackTrace();
        }
    }

    private void reclaim(String targetId, Integer size){
        try{
            Services stub = (Services) this.registry.lookup(this.accessPoint);
            String response = stub.reclaim(targetId, size);
            System.out.println(response);
        }catch(Exception e){
            Logger.ERR(this.getClass().getName(), "Error on requesting reclaim.");
            e.printStackTrace();
        }
    }
}
