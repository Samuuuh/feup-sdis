package sendMessage;

import factory.MessageChunkTemp;
import main.Peer;

import java.io.IOException;
import java.net.MulticastSocket;

public class SendMessageWithChunkNo extends SendMessage {
    String chunkNo;
    public SendMessageWithChunkNo(String version, String type, String fileId, String chunkNo) {
        super(version, type, fileId);
        this.chunkNo = String.valueOf(chunkNo);
    }


    public void run(){
        System.out.println("SendMessageWithChunkNo\t:: Sending message " + type + "...");
        try {
            System.out.println("BackupSubProtoc\t:: Sending multicast requests...");
            MulticastSocket socket = new MulticastSocket();

            byte[] message = new MessageChunkTemp(type, Peer.peer_no, chunkNo).generateHeader();
            sendMessage(socket, message);

            System.out.println("SendMessageWithChunkNo\t:: Message sent!");

            //String received = receiveMessage(socket);
            //displayRequest(received);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
