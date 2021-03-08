package channel;

import factory.MessageParser;
import main.Definitions;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.MulticastSocket;

public class Channel extends Thread {

    protected final String mcast_addr;
    protected final int mcast_port;
    protected final InetAddress group;
    protected final MulticastSocket mcast_socket;
    protected MessageParser messageParser;

    public Channel(int mcast_port, String mcast_addr) throws IOException {
        this.mcast_port = mcast_port;
        this.mcast_addr = mcast_addr;

        // Join the group. 
        mcast_socket = new MulticastSocket(mcast_port);
        this.group = InetAddress.getByName(mcast_addr);
        mcast_socket.joinGroup(group);
    }

    private static void sendMessage(DatagramSocket socket, byte[] message, String host) throws IOException {
        InetAddress address = InetAddress.getByName(host);

        DatagramPacket packet = new DatagramPacket(message, message.length, address, Definitions.PORT);
        socket.send(packet);
    }


    // Receives the messages and sends them to a handler.
    @Override
    public void run() {
        byte[] buf = new byte[Definitions.CHUNK_MAX_SIZE];

        while (true) {
            try {
                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, mcast_port);
                mcast_socket.receive(packet);
                messageParser = new MessageParser(packet.getData());

            } catch (IOException e) {
                e.printStackTrace();
            }

        }
    }
}
