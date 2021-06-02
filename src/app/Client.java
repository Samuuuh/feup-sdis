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

    /**
    * This is the main method which invokes a protocol using the RMI interface
    * @param args Should have following arguments: <peer_ap> <sub_protocol> <opnd_1> <opnd_2>
    */
    public static void main(String[] args) throws IOException {
        if (args.length < 2) {
            System.out.println("Usage:\n java Client <local_ip> <local_port> <sub_protocol> <opnd_1> <opnd_2>\n" +
                    "Where <sub_protocol can be one of the following: BACKUP RESTORE DELETE RECLAIM STATE");
            return;
        }

        new Client(args);
    }

    /**
     * Class constructor.
     * @param args Program arguments passed through command Line
     */
    private Client(String[] args) throws IOException {
        accessPoint = args[0] + ':' + args[1];
        operation = args[2];

        try {
            this.registry = LocateRegistry.getRegistry(Singleton.REGISTER_PORT);
        } catch (RemoteException e) {
            System.err.println("Client exception: " + e.toString());
            e.printStackTrace();
        }

        if (operation.equals("BACKUP")) {
            if (args.length != 5) {
                System.out.println("Usage:\n java Client <ip> <port> BACKUP <file> <replication_degree>\n");
                return;
            }
            
            String file = args[3];
            String replication_degree = args[4];
            boolean isNumber = replication_degree.matches("\\d+");

            if (!isNumber) {
                System.out.println("Replication degree should be an Integer");
                return;
            }

            backup(file, replication_degree);

        } else if (this.operation.equals("RESTORE")) {
            if (args.length != 4) {
                System.out.println("Usage:\n java Client <ip> <port> RESTORE <file>\n");
                return;
            }

            String file = args[3];
            restore(file);

        } else if (this.operation.equals("DELETE")) {
            if (args.length != 4) {
                System.out.println("Usage:\n java Client <ip> <port> DELETE <file>\n");
                return;
            }

            String file = args[3];
            delete(file);

        } else if (this.operation.equals("RECLAIM")) {
            if (args.length != 5){
                System.out.println("Usage:\n java Client <ip> <port> RECLAIM <chordId> <size>\n");
                return;
            }
            String id = args[3];
            Integer size = Integer.parseInt(args[4]);
            reclaim(id, size);
        } else {
            System.out.println("Usage:\n java Client <peer_ap> <sub_protocol> <opnd_1> <opnd_2>\n" +
                    "Where <sub_protocol can be one of the following: BACKUP RESTORE DELETE RECLAIM STATE");
        }
    }

    /**
    * Starts de backup protocol through RMI
    * @param filePath Path of the file to do backup
    * @param replication_degree Replication degree of the file
    */
    private void backup(String filePath, String replication_degree)  {
        try {
            Services stub = (Services) this.registry.lookup(accessPoint);
            int replication = Integer.parseInt(replication_degree);
            String response = stub.backup(filePath, replication);
            System.out.println(response);
        } catch (Exception e) {
            Logger.ERR(this.getClass().getName(), "Error on requesting backup.");
            e.printStackTrace();
        }
    }

    /**
    * Starts the restore protocol through RMI
    * @param filePath Path of the file to restore 
    */
    private void restore(String filePath)  {
        try {
            Services stub = (Services) this.registry.lookup(accessPoint);
            String response = stub.restore(filePath);
            System.out.println(response);
        }catch (Exception e){
            Logger.ERR(this.getClass().getName(), "Error on requesting backup.");
            e.printStackTrace();
        }
    }

    /**
    * Starts the delete protocol through RMI
    * @param filePath Path of the file to delete
    */
    private void delete(String filePath)  {
        try {
            Services stub = (Services) this.registry.lookup(accessPoint);
            String response = stub.delete(filePath);
            System.out.println(response);
        }catch (Exception e){
            Logger.ERR(this.getClass().getName(), "Error on requesting backup.");
            e.printStackTrace();
        }
    }

    /**
    * Starts the reclaim protocol through RMI
    * @param targetId id of the peer where we should use reclaim
    * @param size Reclaim size 
    */
    private void reclaim(String targetId, Integer size){
        try{
            Services stub = (Services) this.registry.lookup(accessPoint);
            String response = stub.reclaim(targetId, size);
            System.out.println(response);
        }catch(Exception e){
            Logger.ERR(this.getClass().getName(), "Error on requesting reclaim.");
            e.printStackTrace();
        }
    }
}
