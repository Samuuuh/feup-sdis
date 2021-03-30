package channel;
import main.Peer;
import main.etc.Singleton;
import process.answer.PrepareStored;
import state.ChunkState;

import java.io.IOException;
import java.net.DatagramPacket;

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

                if (messageParsed.getSenderId().equals(Peer.peer_no))
                    continue;

                if (messageParsed.getMessageType().equals(Singleton.PUTCHUNK)) {
                    if (state.State.totalSpace>= state.State.occupiedSpace+ messageParsed.getData().length) {
                        String chunkId = Singleton.buildChunkId(messageParsed.getFileId(), messageParsed.getChunkNo());
                        ChunkState chunkState = new ChunkState(chunkId, Integer.parseInt(messageParsed.getReplicationDeg()), messageParsed.getData().length/1000);
                        Peer.peer_state.putChunk(chunkId, chunkState);
                        new PrepareStored(messageParsed).start();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
