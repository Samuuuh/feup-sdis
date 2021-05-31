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
    String fileName;
    int port;
    int repDeg;
    InfoNode originNode;
    String hash;

    /** 
    * Resend backup file
    * @param ip ip to send the backup
    * @param port port to send the backup
    * @param originNode origin node which started the backed
    * @param repDeg desired rep deg
    * @param hash Hash of the file.
    */
    public ResendBackupFile(String ip, int port, String hash, InfoNode originNode, int repDeg) {
        this.ip = ip;
        this.port = port;
        this.repDeg = repDeg;
        this.originNode = originNode;
        this.hash = hash;
    }

    /**
     * Run the resend backup message    
     */
    @Override
    public void run() {
        try {
            MessageBackup oldMessage = FileHandler.ReadObjectFromFile(Singleton.getBackupFilePath(hash));
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
