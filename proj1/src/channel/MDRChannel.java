package channel;

import factory.MessageParser;
import main.Definitions;
import main.Peer;

import java.io.IOException;
import java.net.DatagramPacket;

public class MDRChannel extends Channel {
    public MDRChannel(int mcast_port, String mcast_addr) throws IOException {
    super(mcast_port, mcast_addr);
    }

    @Override
    public void run() {
        System.out.println("COMEÇOU A ZOERA MANO");
        while (true) {
            try {
                byte[] buf = new byte[83648];
                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, mcast_port);
                mcast_socket.receive(packet);

                System.out.println("MDR Channel\t:: Packet received."); // Receive PutChunk
                messageParsed = new MessageParser(packet.getData());

                // Checks if message came from the same peer.
                if (messageParsed.getSenderId().equals(Peer.peer_no))
                    continue;

                System.out.println("Cheguei cheguei chegando bagunçando a zorra toda MAS FORA");
                if (messageParsed.getMessageType().equals(Definitions.CHUNK))
                    System.out.println("Cheguei cheguei chegando bagunçando a zorra toda");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
