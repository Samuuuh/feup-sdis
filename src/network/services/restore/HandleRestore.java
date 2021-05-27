package network.services.restore;

import network.Main;
import network.etc.FileHandler;
import network.message.MessageBackup;
import network.message.MessageRcvRestore;
import network.message.MessageRestore;
import network.server.com.SendMessage;

import java.io.IOException;

public class HandleRestore implements Runnable {
    MessageRestore message;
    String port;
    String fileName;

    public HandleRestore(MessageRestore message, int port) {
        this.message = message;
        this.fileName = message.getFileName();
        this.port = String.valueOf(port);
    }

    @Override
    public void run() {
        try {
            if (Main.state.getStoredFile(fileName) != null) {
                MessageBackup mess = FileHandler.ReadObjectFromFile( port + "/backup/" + fileName + ".ser");
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
