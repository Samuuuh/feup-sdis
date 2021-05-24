package network.server.com;

import network.Main;
import network.etc.*;
import network.message.*;
import network.server.fixFingers.PutOnFinger;
import network.node.InfoNode;
import network.server.stabilize.Stabilize;
import network.services.Lookup;
import network.services.backup.ProcessBackup;
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

    public ChordServer(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {

        SSLServerConnection con = null;

        try {
            con = new SSLServerConnection(port);
            while (true) {
                SSLSocket socket = con.accept();
                var out = new ObjectOutputStream(socket.getOutputStream());
                out.writeObject(new OK());
                var in = new ObjectInputStream(socket.getInputStream());
                Message message = (Message) in.readObject();
                MessageType type = message.getType();
                
                if (type == MessageType.RCV_RESTORE) {
                    Logger.ANY(this.getClass().getName(), "Received RCV_RESTORE.");
                    Main.threadPool.execute(new ProcessRestore((MessageRcvRestore) message));
                } else if (type == MessageType.RESTORE) {
                    Logger.ANY(this.getClass().getName(), "Received RESTORE.");
                    // See if peer has the file
                    
                    if(Main.state.getFile(((MessageRestore) message).getFile()) != null) {
                        MessageBackup mess = FileHandler.ReadObjectFromFile(String.valueOf(port) + "backup/file.ser");
                        MessageRcvRestore messageRcvRestore = new MessageRcvRestore(message.getOriginNode(), mess.getBytes(), mess.getFileName());
                        new SendMessage(message.getIpOrigin(), message.getPortOrigin(), messageRcvRestore).call();
                    } else {
                        // Main.threadPool.execute(new SendMessage(message.getIpOrigin(), message.getPortOrigin(), message));
                    }
                    
                } else if (type == MessageType.DONE_BACKUP) {
                    int desiredRepDeg = ((MessageDoneBackup) message).getDesiredRepDeg();
                    int actualRepDeg = ((MessageDoneBackup) message).getActualRepDeg();
                    Logger.ANY(this.getClass().getName(), "Received DONE_BACKUP. Backup finished");

                    if(desiredRepDeg == actualRepDeg) {
                        Logger.ANY(this.getClass().getName(), "Desired replication degree met. RepDeg: " + actualRepDeg);
                    } else {
                        Logger.ANY(this.getClass().getName(), "Desired replication degree not met. Expected:" + desiredRepDeg + " Met:" + actualRepDeg);
                    }       
                }
                else if (type == MessageType.BACKUP) {
                    int desiredRepDeg = ((MessageBackup) message).getDesiredRepDeg();
                    int actualRepDeg = ((MessageBackup) message).getActualRepDeg();
                    
                    Main.state.addFile(((MessageBackup) message).getFileName());

                    if (message.getPortOrigin() == port) {
                        Logger.ANY(this.getClass().getName(), "Backup finished");
                        if(desiredRepDeg == actualRepDeg) {
                            Logger.ANY(this.getClass().getName(), "Desired replication degree met. RepDeg: " + actualRepDeg);
                        } else {
                            Logger.ANY(this.getClass().getName(), "Desired replication degree not met. Expected:" + desiredRepDeg + " Met:" + actualRepDeg);
                        }
                    } else {
                        Main.threadPool.execute(new ProcessBackup((MessageBackup) message));
                    }

                }
                else if (type == MessageType.STORED){
                    Logger.ANY(this.getClass().getName(), "Received stored from peer " + message.getOriginNode().getId());
                }
                else if (type == MessageType.LOOKUP) {
                    Main.threadPool.execute(new Lookup((MessageLookup) message, MessageType.SUCCESSOR, MessageType.LOOKUP));
                } else if (type.equals(MessageType.SUCCESSOR)) {
                    Main.chordNode.setSuccessor(((MessageSuccessor) message).getSuccessor());
                } else if (type == MessageType.NOTIFY) {
                    Main.chordNode.notify((MessageInfoNode) message);
                } else if (type == MessageType.GET_PREDECESSOR) {
                    // Stabilize from another node asking the chordNode predecessor.
                    MessageInfoNode messageInfoNode = new MessageInfoNode(Main.chordNode.getInfoNode(), MessageType.ANS_GET_PREDECESSOR, Main.chordNode.getPredecessor());
                    new SendMessage(message.getIpOrigin(), message.getPortOrigin(), messageInfoNode).call();
                } else if (type == MessageType.ANS_GET_PREDECESSOR) {
                    // Continue the stabilize process after receiving the successor predecessor.
                    Main.threadPool.execute(new Stabilize((MessageInfoNode) message));
                } else if (type == MessageType.FIX_FINGERS) {
                    Main.threadPool.execute(new Lookup((MessageLookup) message, MessageType.ANS_FIX_FINGERS, MessageType.FIX_FINGERS));
                } else if (type == MessageType.ANS_FIX_FINGERS) {
                    Main.threadPool.execute(new PutOnFinger((MessageSuccessor) message));
                } else if (type == MessageType.OK) {
                } else {
                    Logger.ANY(this.getClass().getName(), "Received" + message.getType() + "message");
                }


            }
        } catch (
                Exception ignored) {
            Logger.ANY(this.getClass().getName(), "End ChordServer");
        }

    }

    public int getPort() {
        return port;
    }
}
