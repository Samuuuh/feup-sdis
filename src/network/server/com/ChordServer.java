package network.server.com;

import network.Main;
import network.etc.*;
import network.message.*;
import network.node.InfoNode;
import network.server.stabilize.Stabilize;
import network.services.Lookup;

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

                if (type == MessageType.DONE_BACKUP) {
                    int desiredRepDeg = ((MessageDoneBackup) message).getDesiredRepDeg();
                    int actualRepDeg = ((MessageDoneBackup) message).getActualRepDeg();
                    Logger.ANY(this.getClass().getName(), "Received DONE_BACKUP. Backup finished");

                    if(desiredRepDeg == actualRepDeg) {
                        Logger.ANY(this.getClass().getName(), "Desired replication degree met. RepDeg: " + actualRepDeg);
                    } else {
                        Logger.ANY(this.getClass().getName(), "Desired replication degree not met. Expected:" + desiredRepDeg + " Met:" + actualRepDeg);
                    }       
                }
                if (type == MessageType.BACKUP) {
                    Logger.ANY(this.getClass().getName(), "Received BACKUP");
                    int desiredRepDeg = ((MessageBackup) message).getDesiredRepDeg();

                    if (message.getPortOrigin() == port) {
                        int actualRepDeg = ((MessageBackup) message).getActualRepDeg();
        
                        Logger.ANY(this.getClass().getName(), "Backup finished");
                        if(desiredRepDeg == actualRepDeg) {
                            Logger.ANY(this.getClass().getName(), "Desired replication degree met. RepDeg: " + actualRepDeg);
                        } else {
                            Logger.ANY(this.getClass().getName(), "Desired replication degree not met. Expected:" + desiredRepDeg + " Met:" + actualRepDeg);
                        }
                        continue;
                    }
                    String filePath = ((MessageBackup) message).getFileName();
                    byte[] bytesMessage = ((MessageBackup) message).getBytes();
                    FileHandler.saveFile(port + "/backup/", filePath, bytesMessage);
                    
                    // TODO: Verificar se o peer pode salvar/salvou o ficheiro
                    int actualRepDeg = ((MessageBackup) message).getActualRepDeg() + 1;

                    if(actualRepDeg == desiredRepDeg) {
                        MessageDoneBackup messageDone = new MessageDoneBackup(message.getOriginNode(), MessageType.DONE_BACKUP, desiredRepDeg, actualRepDeg);
                        Main.threadPool.execute(new SendMessage(message.getIpOrigin(), message.getPortOrigin(), messageDone));
                    } else {
                        InfoNode suc = Main.chordNode.getSuccessor();
                        MessageBackup newMessage = new MessageBackup(message.getOriginNode(), filePath, bytesMessage, desiredRepDeg, actualRepDeg);
                        Main.threadPool.execute(new SendMessage(suc.getIp(), suc.getPort(), newMessage));
                    }
                    
                    // OR SAVE THE SERIALIZE FILE
                    /*
                    FileOutputStream fileOut = new FileOutputStream(((MessageBackup) message).getFileName() + ".ser");
                    ObjectOutputStream outBackup = new ObjectOutputStream(fileOut);
                    outBackup.writeObject(message);
                    outBackup.close();
                    fileOut.close();
                    */
                    
                    // TODO: Check if repdeg is met
                    // otherwise send to the sucessor
                } else if (type == MessageType.LOOKUP) {
                    Main.threadPool.execute(new Lookup((MessageLookup) message, MessageType.SUCCESSOR, MessageType.LOOKUP));
                } else if (type.equals(MessageType.SUCCESSOR)) {
                    Main.chordNode.setSuccessor(((MessageInfoNode) message).getInfoNode());
                }
                else if (type == MessageType.NOTIFY){
                    Main.chordNode.notify((MessageInfoNode) message);
                }
                else if (type == MessageType.GET_PREDECESSOR){
                    // Stabilize from another node asking the chordNode predecessor.
                    MessageInfoNode messageInfoNode = new MessageInfoNode(Main.chordNode.getInfoNode(), MessageType.ANS_GET_PREDECESSOR, Main.chordNode.getPredecessor());
                    Main.threadPool.execute(new SendMessage(message.getIpOrigin(), message.getPortOrigin(), messageInfoNode));
                }
                else if (type == MessageType.ANS_GET_PREDECESSOR){
                    // Continue the stabilize process after receiving the successor predecessor.
                    Main.threadPool.execute(new Stabilize((MessageInfoNode) message));
                } else if (type == MessageType.FIX_FINGERS){
                    Logger.ANY(this.getClass().getName(), "Received FIX_FINGERS");
                } else if (type == MessageType.ANS_FIX_FINGERS) {
                    Logger.ANY(this.getClass().getName(), "Received ANS_FIX_FINGERS");
                }
                else
                    Logger.ANY(this.getClass().getName(), "Received"+ message.getType() + "message");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public int getPort() {
        return port;
    }
}
