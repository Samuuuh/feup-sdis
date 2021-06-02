package network.services.restore;

import network.Main;
import network.etc.FileHandler;
import network.etc.Singleton;
import network.message.MessageBackup;
import network.message.MessageRcvRestore;
import network.message.MessageRestore;
import network.node.InfoNode;
import network.server.com.SendMessage;

import java.io.IOException;

public class HandleRestore implements Runnable {
    MessageRestore message;
    String port;
    String fileName;
    String hash;

    /**
     * Handle Restore constructor
     * @param message restore message
     * @param port port to send the message
     */
    public HandleRestore(MessageRestore message, int port) {
        this.message = message;
        this.fileName = message.getFileName();
        this.hash = Singleton.hash(fileName);
        this.port = String.valueOf(port);
    }

    /**
    * Run the handle restor
    */
    @Override
    public void run() {
        try {
            InfoNode suc = Main.chordNode.getSuccessor();
            
            if (Main.state.getStoredFile(hash) != null) {
                MessageBackup mess = FileHandler.ReadObjectFromFile(Singleton.getBackupFilePath(hash));
                MessageRcvRestore messageRcvRestore = new MessageRcvRestore(message.getOriginNode(), this.hash, mess.getBytes(), mess.getFileName());
                System.out.println("read file and sending to origin");
                Main.threadPool.submit(new SendMessage(message.getIpOrigin(), message.getPortOrigin(), messageRcvRestore));
            } else {
                Main.threadPool.submit(new SendMessage(suc.getIp(), suc.getPort(), message));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
