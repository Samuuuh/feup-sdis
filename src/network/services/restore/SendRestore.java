package network.services.restore;

import network.server.com.*;
import network.message.*;
import network.node.InfoNode;
import network.etc.FileHandler;

import java.io.IOException;

public class SendRestore implements Runnable {
    String ip;
    String filePath;
    int port;
    int repDeg;
    InfoNode originNode;

    public SendRestore(String ip, int port, String filePath, InfoNode originNode) {
         this.ip = ip;
         this.port = port;
         this.filePath = filePath;
         this.originNode = originNode;
    }

    @Override
    public void run() {
        MessageRestore message = new MessageRestore(originNode, filePath);
        new SendMessage(ip, port, message).start();
    }
}
