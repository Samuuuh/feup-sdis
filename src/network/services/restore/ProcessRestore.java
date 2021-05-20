package network.services.restore;

import network.Main;
import network.etc.FileHandler;
import network.etc.Logger;
import network.etc.MessageType;
import network.message.MessageBackup;
import network.message.MessageStored;
import network.message.MessageDoneBackup;
import network.message.MessageRcvRestore;
import network.node.InfoNode;
import network.server.com.SendMessage;

public class ProcessRestore implements Runnable {
    MessageRcvRestore message;

    public ProcessRestore(MessageRcvRestore messageRestore) {
        this.message = messageRestore;
    }

    @Override
    public void run() {
        Logger.ANY(this.getClass().getName(), "Start RESTORE");
        String filePath = message.getFileName();

        saveFile(filePath, message);
    }

    public void saveFile(String filePath, MessageRcvRestore message) {
        try {
            int port = Main.chordNode.getInfoNode().getPort();
            FileHandler.saveFile(port + "/restore/", filePath, message.getBytes());
        }catch(Exception e) {
            Logger.ERR(this.getClass().getName(), "Not possible to save file " + filePath);
        }
    }
}
