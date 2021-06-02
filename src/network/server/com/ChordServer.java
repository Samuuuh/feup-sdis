package network.server.com;

import network.Main;
import network.etc.*;
import network.message.*;
import network.message.MessageBackup;
import network.message.MessageDoneBackup;
import network.message.reclaim.MessageReclaim;
import network.server.fixFingers.PutOnFinger;
import network.server.stabilize.Stabilize;
import network.services.Lookup;
import network.services.backup.ProcessBackup;
import network.services.reclaim.ProcessReclaim;
import network.services.delete.ProcessDelete;
import network.services.restore.HandleRestore;
import network.services.restore.ProcessRestore;

import javax.net.ssl.SSLSocket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Server channel. It receives messages from other peers.
 */
public class ChordServer extends Thread {

    int port;
    String ip;

    /**
    * ChordServer class constructor
    * @param ip ip of the server
    * @param port Port of the server
    */
    public ChordServer(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }
    
    /**
    * Get the port of peer
    * @return int Port number
    */
    public int getPort() {
        return port;
    }

    /**
     * This function prints if the replication was achieved or not.
     * @param desiredRepDeg Desired rep deg of the file
     * @param actualRepDeg Atual rep deg of the file
     */
    private void backupEndLog(int desiredRepDeg, int actualRepDeg){
        Logger.ANY(this.getClass().getName(), "Backup finished");
        if (desiredRepDeg == actualRepDeg) {
            Logger.ANY(this.getClass().getName(), "Desired replication degree met. RepDeg: " + actualRepDeg);
        } else {
            Logger.ANY(this.getClass().getName(), "Desired replication degree not met. Expected:" + desiredRepDeg + " Met:" + actualRepDeg);
        }
    }

    /**
    * Run the ChordServer
    */
    @Override
    public void run() {
        SSLServerConnection con = null;

        try {
            con = new SSLServerConnection(port);
        } catch (Exception e) {
            System.out.println("Not possible to initialize port connection with port : " + port);
        }

        while (true) {
            try {
                assert con != null;
                SSLSocket socket = con.accept();
                var out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(new OK());
                var in = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) in.readObject();
                MessageType type = message.getType();


                switch (type) {
                    case BACKUP:
                        var messageBackup = ((MessageBackup) message);
                        if ((message.getPortOrigin() == port) && (message.getIpOrigin().equals(ip))) {
                            backupEndLog(messageBackup.getDesiredRepDeg(), messageBackup.getActualRepDeg());
                        } else {
                            Main.threadPool.execute(new ProcessBackup(messageBackup));
                        }
                        break;
                    case STORED:
                        Logger.ANY(this.getClass().getName(), "Received stored from peer " + message.getOriginNode().getId());
                        break;
                    case DONE_BACKUP:
                        Main.state.printStoredFiles();
                        var messageDoneBackup = (MessageDoneBackup) message;
                        backupEndLog(messageDoneBackup.getDesiredRepDeg(), messageDoneBackup.getActualRepDeg());
                        break;

                    // Check if it has the file, otherwise pass the message to the successor.
                    case RESTORE:
                        Main.threadPool.execute(new HandleRestore((MessageRestore) message, port));
                        break;
                    // Confirmation.
                    case RCV_RESTORE:
                        Logger.ANY(this.getClass().getName(), "Received RCV_RESTORE.");
                        Main.threadPool.execute(new ProcessRestore((MessageRcvRestore) message));
                        break;
                    case DELETE:
                        Main.threadPool.execute(new ProcessDelete((MessageDelete) message, port));
                        break;
                    case LOOKUP:
                        Main.threadPool.execute(new Lookup((MessageLookup) message, MessageType.SUCCESSOR, MessageType.LOOKUP));
                        break;
                    case SUCCESSOR:
                        Main.chordNode.setSuccessor(((MessageSuccessor) message).getSuccessor());
                        break;
                    case NOTIFY:
                        Main.chordNode.notify((MessageInfoNode) message);
                        break;
                    case GET_PREDECESSOR:
                        MessageInfoNode messageInfoNode = new MessageInfoNode(Main.chordNode.getInfoNode(), MessageType.ANS_GET_PREDECESSOR, Main.chordNode.getPredecessor());
                        Main.threadPool.submit(new SendMessage(message.getIpOrigin(), message.getPortOrigin(), messageInfoNode));
                        break;
                    case ANS_GET_PREDECESSOR:
                        Main.threadPool.execute(new Stabilize((MessageInfoNode) message));
                        break;
                    case FIX_FINGERS:
                        Main.threadPool.execute(new Lookup((MessageLookup) message, MessageType.ANS_FIX_FINGERS, MessageType.FIX_FINGERS));
                        break;
                    case ANS_FIX_FINGERS:
                        Main.threadPool.execute(new PutOnFinger((MessageSuccessor) message));
                        break;
                    case RECLAIM:
                        Main.threadPool.execute(new ProcessReclaim((MessageReclaim) message));
                    case OK:
                        break;
                    default:
                        Logger.ANY(this.getClass().getName(), "Received" + message.getType() + "message");
                        break;
                }
            } catch (Exception e) {
                Logger.ANY(this.getClass().getName(), "Error on ChordServer, still active...");
            }

        }
    }
}
