package network.services.restore;

import network.Main;
import network.etc.FileHandler;
import network.etc.Singleton;
import network.message.MessageBackup;
import network.message.MessageRcvRestore;
import network.message.MessageRestore;
import network.server.com.SendMessage;

import java.io.IOException;

public class HandleRestore implements Runnable {
    MessageRestore message;
    String port;
    String filePath;

    public HandleRestore(MessageRestore message, int port) {
        this.message = message;
        this.filePath = message.getFileName();
        this.port = String.valueOf(port);
    }

    @Override
    public void run() {
        try {
            if (Main.state.getStoredFile(filePath) != null) {
                String fileName = Singleton.getFileName(this.filePath);
                MessageBackup mess = FileHandler.ReadObjectFromFile( Singleton.getBackupFilePath(fileName));
                MessageRcvRestore messageRcvRestore = new MessageRcvRestore(message.getOriginNode(), mess.getBytes(), mess.getFileName());
                Main.threadPool.submit(new SendMessage(message.getIpOrigin(), message.getPortOrigin(), messageRcvRestore));
            } else {
                Main.threadPool.submit(new SendMessage(message.getIpOrigin(), message.getPortOrigin(), message));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
