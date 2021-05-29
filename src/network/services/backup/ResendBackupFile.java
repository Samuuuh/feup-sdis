package network.services.backup;

import network.Main;
import network.etc.FileHandler;
import network.etc.Logger;
import network.etc.Singleton;
import network.message.MessageBackup;
import network.node.InfoNode;
import network.server.com.SendMessage;

import java.io.IOException;

public class ResendBackupFile implements Runnable {
    String ip;
    String filePath;
    int port;
    int repDeg;
    InfoNode originNode;

    public ResendBackupFile(String ip, int port, String filePath, InfoNode originNode, int repDeg) {
        this.ip = ip;
        this.port = port;
        this.filePath = filePath;
        this.repDeg = repDeg;
        this.originNode = originNode;
    }

    @Override
    public void run() {
        try {
            MessageBackup oldMessage = FileHandler.ReadObjectFromFile(this.filePath);
            if (oldMessage == null) return;
            MessageBackup newMessage = new MessageBackup(Main.chordNode.getInfoNode(), oldMessage.getFileName(), oldMessage.getBytes(), 1,0);
            Logger.REQUEST(this.getClass().getName(), "Sent message backup");
            Main.threadPool.submit(new SendMessage(ip, port, newMessage));
        } catch (IOException e) {
            Logger.ERR(this.getClass().getName(), "Not possible to send backup message");
            e.printStackTrace();
        }
    }
}
