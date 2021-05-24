package network.server.com;

import network.Main;
import network.etc.*;
import network.message.*;
import network.server.fixFingers.PutOnFinger;
import network.server.stabilize.Stabilize;
import network.services.Lookup;

import javax.net.ssl.SSLSocket;

import java.io.FileOutputStream;
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

                if (type == MessageType.RESTORE) {
                    // TO DO
                }
                if (type == MessageType.BACKUP) {
                    // THIS (Can also be used for restore)
                    byte[] bytesMessage = ((MessageBackup) message).getBytes();
                    FileHandler.saveFile(port + "/backup/", ((MessageBackup) message).getFileName(), bytesMessage);
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
