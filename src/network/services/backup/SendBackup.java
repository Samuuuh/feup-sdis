package network.services.backup;

import network.Main;
import network.etc.Logger;
import network.message.MessageBackup;
import network.server.com.*;
import network.node.InfoNode;
import network.etc.FileHandler;

import java.io.IOException;

public class SendBackup implements Runnable {
    String ip;
    String filePath;
    int port;
    int repDeg;
    InfoNode originNode;

    /** 
     * Send backup message
     * @param ip ip to send the message
     * @param port port to send the backup message 
     * @param originNode information about the node that
     * @param repDeg replication degree of the file
     */
    public SendBackup(String ip, int port, String filePath, InfoNode originNode, int repDeg) {
         this.ip = ip;
         this.port = port;
         this.filePath = filePath;
         this.repDeg = repDeg;
         this.originNode = originNode;
    }

    /**
     * Send the backup message
     */
    @Override
    public void run() {
        try {
            byte[] byteArr = FileHandler.readFile(filePath);
            if (byteArr == null) return;
            Logger.REQUEST(this.getClass().getName(), "Sent message backup");
            MessageBackup message = new MessageBackup(originNode, filePath, byteArr, repDeg, 0);
            Main.threadPool.submit(new SendMessage(ip, port, message));
        } catch (IOException e) {
            Logger.ERR(this.getClass().getName(), "Not possible to send backup message");
            e.printStackTrace();
        }
    }
}
