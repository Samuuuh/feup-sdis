package channel;

import factory.MessageParser;
import main.Definitions;
import main.Peer;

import java.io.IOException;
import java.net.DatagramPacket;

public class MCChannel extends Channel {
    // TODO:
    // STORED
    // GETCHUNK
    // DELETE
    // REMOVE
    public MCChannel(int mcast_port, String mcast_addr) throws IOException {
        super(mcast_port, mcast_addr);
    }

    @Override
    public void run() {
        while (true) {
            try {
                byte[] buf = new byte[83648];
                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, mcast_port);
                receivePacket(mcast_socket, packet);

                System.out.println("Control Channel\t:: Packet received."); // Receive PutChunk
                messageParsed = new MessageParser(packet.getData());

                // Checks if message came from the same peer.
                if (messageParsed.getSenderId().equals(Peer.peer_no))
                    continue;

                // Treats the message.
                if (messageParsed.getMessageType().equals(Definitions.STORED)) {
                    System.out.println("Received stored");

                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
