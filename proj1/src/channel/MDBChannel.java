package channel;
import main.Definitions;
import main.Peer;
import process.answer.PrepareStored;
import java.net.DatagramPacket;

import java.io.IOException;

// TODO: MDB
public class MDBChannel extends Channel {
    public MDBChannel(int mcast_port, String mcast_addr) throws IOException {
        super(mcast_port, mcast_addr);
    }

    @Override
    public void run() {
        while (true) {
            try {
                byte[] buf = new byte[83648];
                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, mcast_port);
                mcast_socket.receive(packet);

                messageParsed = new MessageParser(packet.getData());

                // Checks if message came from the same peer.
                if (messageParsed.getSenderId().equals(Peer.peer_no))
                    continue;

                // Treats the message.
                if (messageParsed.getMessageType().equals(Definitions.PUTCHUNK))
                    new PrepareStored(messageParsed).start();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
