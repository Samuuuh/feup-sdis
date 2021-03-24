package channel;

import main.Definitions;
import main.Peer;
import process.answer.PrepareChunk;
import process.postAnswer.DeleteChunk;

import java.io.IOException;
import java.net.DatagramPacket;

// TODO: STORED, GETCHUNK, DELETE, REMOVE
public class MCChannel extends Channel {
    public MCChannel(int mcast_port, String mcast_addr) throws IOException {
        super(mcast_port, mcast_addr);
    }

    @Override
    public void run() {
        while (true) {
            try {
                byte[] buf = new byte[83648];
                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, mcast_port);
                mcast_socket.receive(packet);

                System.out.println("MC Channel\t:: Packet received."); // Receive PutChunk
                messageParsed = new MessageParser(packet.getData());

                // Checks if message came from the same peer.
                if (messageParsed.getSenderId().equals(Peer.peer_no))
                    continue;

                // Treats the message.
                if (messageParsed.getMessageType().equals(Definitions.STORED)) {
                    String fileId = messageParsed.getFileId();
                    Peer.peer_state.increaseRepDeg( fileId, fileId + "-" + messageParsed.getChunkNo());
                }
                else if (messageParsed.getMessageType().equals(Definitions.GETCHUNK)) {
                    new PrepareChunk(messageParsed.getFileId(), messageParsed.getChunkNo()).start();
                }
                else if (messageParsed.getMessageType().equals(Definitions.DELETE)) {
                    System.out.println();
                    new DeleteChunk(messageParsed.getFileId()).start();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}
