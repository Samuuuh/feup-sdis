package network.services.backup;

import network.server.com.*;
import network.message.*;
import network.node.InfoNode;
import network.*;
import network.etc.FileHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ProtocolException;

public class SendBackup implements Runnable{
    String ip;
    String filePath;
    int port;
    int repDeg;
    InfoNode originNode;

    public SendBackup(String ip, int port, String filePath, InfoNode originNode, int repDeg) {
         this.ip = ip;
         this.port = port;
         this.filePath = filePath;
         this.repDeg = repDeg;
         this.originNode = originNode;
    }

    @Override
    public void run() {
        try {
            byte[] byteArr = FileHandler.readFile(filePath);
            MessageBackup message = new MessageBackup(originNode, filePath, byteArr, repDeg, 0);
            new SendMessage(ip, port, message).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
