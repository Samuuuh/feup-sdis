package subProtocol;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import main.Peer;


public abstract class SubProtocol extends Thread {
    public SubProtocol() {
    }

    public static void sendPacket(byte[] message) throws IOException {
        MulticastSocket socket = new MulticastSocket(Peer.mcast_port);
        InetAddress group = InetAddress.getByName(Peer.mcast_addr);
        DatagramPacket packet = new DatagramPacket(message, message.length, group, Peer.mcast_port);

        socket.send(packet);
    }
}
