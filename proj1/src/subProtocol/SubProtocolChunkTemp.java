package subProtocol;

import factory.MessageChunkTemp;
import main.Peer;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class SubProtocolChunkTemp extends SubProtocol{

    String chunkNo;
    public SubProtocolChunkTemp(String version, String type, String fileId, String chunkNo) {
        super(version, type, fileId);
        this.chunkNo = String.valueOf(chunkNo);
    }


    public void run(){
        System.out.println("Subprotocol\t:: Sending message " + type + "...");
        try {
            System.out.println("BackupSubProtoc\t:: Sending multicast requests...");
            MulticastSocket socket = new MulticastSocket();

            byte[] message = new MessageChunkTemp(type, Peer.peer_no, chunkNo).generateHeader();
            sendMessage(socket, message);

            System.out.println("BackupSubProtoc\t:: Message sent!");

            //String received = receiveMessage(socket);
            //displayRequest(received);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
