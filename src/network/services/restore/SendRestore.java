package network.services.restore;

import network.Main;
import network.etc.Logger;
import network.etc.Singleton;
import network.server.com.*;
import network.message.*;
import network.node.InfoNode;

public class SendRestore implements Runnable {
    String ip;
    String fileName;
    int port;
    int repDeg;
    InfoNode originNode;

    /**
     * Send restore message constructor
     * @param ip ip which restore will be sent
     * @param port port which restore will be sent
     * @param fileName path of the file
     * @param originNode node that started the restore
     */
    public SendRestore(String ip, int port, String filePath, InfoNode originNode) {
         this.ip = ip;
         this.port = port;
         this.fileName = Singleton.getFileName(filePath) + "." + Singleton.getFileExtension(filePath);
         this.originNode = originNode;
    }

    /**
     * Run the send restore message
     */
    @Override
    public void run() {
        try {
            MessageRestore message = new MessageRestore(originNode, fileName);
            Main.threadPool.submit(new SendMessage(ip, port, message));
        }catch(Exception e){
            Logger.ERR(this.getClass().getName(), "Not possible to send restore message");
        }
    }
}
