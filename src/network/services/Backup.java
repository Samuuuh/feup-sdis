package network.services;

import network.server.com.*;
import network.message.*;
import network.node.InfoNode;
import network.*;
import network.etc.FileHandler;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;

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
    public void request(InfoNode originNode) throws IOException, ClassNotFoundException {
        byte[] byteArr = FileHandler.readFile(filePath);

        
        MessageBackup message = new MessageBackup(originNode, filePath, byteArr);

        // TODO: create a for loop for the replication degree
        // TODO: which one to send the backup message
        new SendMessage("127.0.0.1", 8888, message).call();
    }
}
