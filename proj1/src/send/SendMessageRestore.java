package send;

import factory.MessageChunkNo;
import factory.MessageRestore;
import main.Peer;

import java.io.IOException;
import java.net.MulticastSocket;

public class SendMessageRestore extends SendMessage {
    String chunkNo;
    byte[] body;


    public SendMessageRestore(String version, String type, String fileId, byte[] body, String chunkNo) {
        super(version, type, fileId);
        this.body = body;
        this.chunkNo = chunkNo;
    }

    public void run(){
        System.out.println("SendMessageRestore\t:: Sending message " + type + "...");
        try {
            System.out.println("Restore\t:: Sending multicast requests...");
            MulticastSocket socket = new MulticastSocket();

            byte[] message = new MessageRestore(fileId, body, chunkNo).createMessage();

            sendMessage(socket, message, Peer.mdr_addr, Peer.mdr_port);

            System.out.println("SendMessageRestore\t:: Message sent!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
