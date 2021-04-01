package channel;

import main.etc.Logger;
import main.etc.Singleton;
import main.Peer;
import process.answer.PrepareChunk;
import process.postAnswer.DeleteChunk;
import process.postAnswer.RemoveCheck;
import process.request.RequestPutChunk;
import state.ChunkState;
import tasks.backup.BackupChunkCheck;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Timer;

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

                String fileId = messageParsed.getFileId();
                String chunkId = Singleton.getChunkId(fileId, messageParsed.getChunkNo());
                if (messageParsed.getMessageType().equals(Singleton.STORED) ) {
                    Peer.peer_state.updateChunkState(chunkId, messageParsed.getSenderId());
                    Peer.peer_state.updateFileState(fileId, messageParsed.getChunkNo(), messageParsed.getSenderId());
                    Peer.peer_state.printState();
                    Logger.SUC(this.getClass().getName(), "STORED " + chunkId + " on PEER " + messageParsed.getSenderId());
                }

                else if (messageParsed.getMessageType().equals(Singleton.GETCHUNK)) {
                    new PrepareChunk(messageParsed.getFileId(), messageParsed.getChunkNo()).start();
                }

                else if (messageParsed.getMessageType().equals(Singleton.DELETE)) {
                    new DeleteChunk(messageParsed.getFileId()).start();
                }

                else if (messageParsed.getMessageType().equals(Singleton.REMOVED)){
                    new RemoveCheck(fileId, chunkId, messageParsed.getSenderId()).start();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


}
