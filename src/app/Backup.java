package app;

import service.client.SSLConnection;
import service.message.Message;
import service.message.MessageBackup;
import service.message.MessageHello;
import service.utils.FileHandler;

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
    public void request() throws IOException, ClassNotFoundException {
        InetAddress inetIp = InetAddress.getByName(ip);
        byte[] fileBytes = FileHandler.readFile(this.filePath);
        Message messageBackup = new MessageBackup(ip, port, fileBytes);
        SSLConnection sslConnection = new SSLConnection(inetIp, port);

        // TODO: create a for loop for the replication degree
        sslConnection.sendMessage(messageBackup);
        MessageHello response = (MessageHello) sslConnection.readMessage();
        System.out.println(response.getType());
        sslConnection.closeIn();

    }






}
