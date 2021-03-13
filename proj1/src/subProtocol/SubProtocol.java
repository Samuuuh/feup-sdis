package subProtocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

import factory.BackupMessageFactory;
import factory.MessageChunkTemp;
import factory.MessageFactory;
import main.Peer;


public abstract class SubProtocol extends Thread {
    protected String version;
    protected String type;
    protected String fileId;
    protected int chunkNo;

    public SubProtocol(String version, String type, String fileId, int chunkNo) {
        this.version = version;
        this.type = type;
        this.fileId = fileId;
        this.chunkNo = chunkNo;
    }

    @Override
    public void run(){
        System.out.println("Subprotocol\t:: Sending message " + type + "...");
        try {

            System.out.println("BackupSubProtoc\t:: Sending multicast requests...");
            MulticastSocket socket = new MulticastSocket();

            byte[] message = new MessageChunkTemp(type, Peer.peer_no, String.valueOf(chunkNo)).generateHeader();
            sendMessage(socket, message);

            System.out.println("BackupSubProtoc\t:: Message sent!");

            //String received = receiveMessage(socket);
            //displayRequest(received);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    protected static void sendMessage(DatagramSocket socket, byte[] message) throws IOException {
        InetAddress address = InetAddress.getByName(Peer.mcast_addr);

        DatagramPacket packet = new DatagramPacket(message, message.length, address, Peer.mcast_port);
        socket.send(packet);
    }


}
