package channel;

import main.etc.Logger;
import main.etc.Singleton;
import main.Peer;
import process.answer.PrepareChunk;
import process.postAnswer.DeleteChunk;

import java.io.IOException;
import java.net.DatagramPacket;

// STORED, GETCHUNK, DELETE, REMOVE
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
                messageParsed = new MessageParser(packet.getData());

                if (messageParsed.getSenderId().equals(Peer.peer_no))
                    continue;

                if (messageParsed.getMessageType().equals(Singleton.STORED)) {
                    String chunkId = Singleton.buildChunkId(messageParsed.getFileId(), messageParsed.getChunkNo());
                    Peer.peer_state.increaseRepDeg( messageParsed.getFileId(), chunkId);
                    Logger.SUC(this.getClass().getName(), "STORED " + chunkId + " on PEER " + messageParsed.getSenderId());
                }

                else if (messageParsed.getMessageType().equals(Singleton.GETCHUNK)) {
                    new PrepareChunk(messageParsed.getFileId(), messageParsed.getChunkNo()).start();
                }

                else if (messageParsed.getMessageType().equals(Singleton.DELETE)) {
                    new DeleteChunk(messageParsed.getFileId()).start();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}
