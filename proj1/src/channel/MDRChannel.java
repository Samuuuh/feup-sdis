package channel;

import tasks.restore.RestoreWaiting;
import main.Peer;
import main.etc.Singleton;
import process.StoreChunk;

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
                messageParsed = new MessageParser(packet.getData(), packet.getLength());

                if (messageParsed.getSenderId().equals(Peer.peer_no))
                    continue;

                if (messageParsed.getMessageType().equals(Singleton.CHUNK)) {
                    // Abort if exists the task to restore the chunk.
                    String chunkId = Singleton.getChunkId(messageParsed.getFileId(), messageParsed.getChunkNo());
                    Peer.restoreTasks.abortTask(chunkId);
                    // Store the chunk locally.

                    if (RestoreWaiting.isWaitingToRestore(chunkId))
                        new StoreChunk(messageParsed).start();
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
