package network.server.com;

import network.Main;
import network.etc.*;
import network.message.*;
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


                if (type == MessageType.BACKUP) {
                    System.out.println(message.getIpOrigin());
                    byte[] test = ((MessageBackup) message).getBytes();
                    System.out.println(test.length);
                    FileHandler.saveFile("/img", "/test", test);
                    System.out.println("Send Backup to other peers!");
   
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
