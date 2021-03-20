package send;

import factory.MessageChunkNo;
import main.Peer;

import java.io.IOException;
import java.net.MulticastSocket;

public class SendMessageChunkNo extends SendMessage {
    String chunkNo;
    public SendMessageChunkNo(String version, String type, String fileId, String chunkNo) {
        super(version, type, fileId);
        this.chunkNo = String.valueOf(chunkNo);
    }

    public void run(){
        System.out.println("SendMessageWithChunkNo\t:: Sending message " + type + "...");
        try {
            System.out.println("BackupSubProtoc\t:: Sending multicast requests...");
            MulticastSocket socket = new MulticastSocket();

            byte[] message = new MessageChunkNo(type, fileId, chunkNo).generateHeader();

            sendMessage(socket, message, Peer.mc_addr, Peer.mc_port);

            System.out.println("SendMessageWithChunkNo\t:: Message sent!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
