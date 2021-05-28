package network.services.delete;

import network.Main;
import network.etc.Logger;
import network.server.com.*;
import network.message.*;
import network.node.InfoNode;

public class SendDelete implements Runnable {
    String ip;
    String filePath;
    int port;
    int repDeg;
    InfoNode originNode;

    public SendDelete(String ip, int port, String filePath, InfoNode originNode) {
         this.ip = ip;
         this.port = port;
         this.filePath = filePath;
         this.originNode = originNode;
    }

    @Override
    public void run() {
        try {
            MessageDelete message = new MessageDelete(originNode, filePath);
            Main.threadPool.submit(new SendMessage(ip, port, message));
        }catch(Exception e){
            Logger.ERR(this.getClass().getName(), "Not possible to send restore message");
        }
    }
}
