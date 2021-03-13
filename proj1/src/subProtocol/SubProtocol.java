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

    public SubProtocol(String version, String type, String fileId) {
        this.version = version;
        this.type = type;
        this.fileId = fileId;
    }


    protected static void sendMessage(DatagramSocket socket, byte[] message) throws IOException {
        InetAddress address = InetAddress.getByName(Peer.mcast_addr);

        DatagramPacket packet = new DatagramPacket(message, message.length, address, Peer.mcast_port);
        socket.send(packet);
    }


}
