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
                String type = message.getType();


                if (type.equals("backup"))
                    Logger.ANY(this.getClass().getName(), "Received backup message.");
                else if (type.equals("delete"))
                    Logger.ANY(this.getClass().getName(), "Received delete message.");
                else if (type.equals("restore"))
                    Logger.ANY(this.getClass().getName(), "Received restore message");
                else if (type.equals("lookup"))
                    Main.threadPool.execute(new Lookup((MessageLookup) message));
                else if (type.equals("successor")) {
                    Main.chordNode.setSuccessor(((MessageInfoNode) message).getInfoNode());
                }
                else if (type.equals("notify")){
                    Main.chordNode.notify((MessageInfoNode) message);
                }
                else if (type.equals("getPredecessor")){
                    // Stabilize from another node asking the chordNode predecessor.
                    MessageInfoNode messageInfoNode = new MessageInfoNode(Main.chordNode.getInfoNode(), "ansGetPredecessor", Main.chordNode.getPredecessor());
                    Main.threadPool.execute(new SendMessage(message.getIpOrigin(), message.getPortOrigin(), messageInfoNode));
                }
                else if (type.equals("ansGetPredecessor")){
                    // Continue the stabilize process after receiving the successor predecessor.
                    Main.threadPool.execute(new Stabilize((MessageInfoNode) message));
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
