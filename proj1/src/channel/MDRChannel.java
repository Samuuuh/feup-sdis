package channel;

import main.Definitions;
import main.Peer;
import main.Utils;
import process.postAnswer.StoreChunk;

import java.io.IOException;
import java.net.DatagramPacket;

public class MDRChannel extends Channel {
    public MDRChannel(int mcast_port, String mcast_addr) throws IOException {
    super(mcast_port, mcast_addr);
    }

    @Override
    public void run() {
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

                if (messageParsed.getMessageType().equals(Definitions.CHUNK) && Peer.isWaitingToRestore(messageParsed.getFileId())) {
                    System.out.println("Received CHUNK");
                    // Abort if exists the task to restore the chunk.
                    String chunkId = Utils.buildChunkId(messageParsed.getFileId(), messageParsed.getChunkNo());
                    Peer.abortRestoreSchedule(chunkId);
                    // Store all the chunk locally.
                    new StoreChunk(messageParsed).start();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
