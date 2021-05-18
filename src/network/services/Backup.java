package network.services;

import network.server.com.*;
import network.message.*;
import network.node.InfoNode;
import network.*;
import network.etc.FileHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ProtocolException;

public class Backup {
    String ip;
    String filePath;
    int port;
    int repDeg;

    public Backup(String ip, int port, String filePath, int repDeg) {
         this.ip = ip;
         this.port = port;
         this.filePath = filePath;
         this.repDeg = repDeg;
    }

    /**
     * This function is responsible for sending the message to the SSLServer .
     */
    public void request(InfoNode originNode, int repDeg) throws IOException, ClassNotFoundException {
        byte[] byteArr = FileHandler.readFile(filePath);

        MessageBackup message = new MessageBackup(originNode, filePath, byteArr, repDeg, 0);

        new SendMessage(ip, port, message).start();
    }
}
