package sdis.project; 

import java.io.*;
import java.net.*;
import java.util.*;
 
public class Client {
    public static void main(String[] args) throws IOException {
       if (args.length < 2) {
             System.out.println("Usage:\n java Client <peer_ap> <sub_protocol> <opnd_1> <opnd_2>\n" +
                                "Where <sub_protocol can be one of the following: BACKUP RESTORE DELETE RECLAIM STATE");
             return;
        }

        Client client = new Client(args);
    }

    private String peerAccessPoint;
    private String operation;

    private Client(String[] args) {
        this.peerAccessPoint = args[0];
        this.operation = args[1];

        if(this.operation.equals("BACKUP")) {
            if (args.length != 4) {
                System.out.println("Usage:\n java Client <peer_ap> BACKUP <file> <replication_degree>\n");
                return;
            }

            String file = args[2];
            String replication_degree = args[3];
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

    private void backup(String file, String replication_degree) {
        System.out.println("its backup boiiiiiis");
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