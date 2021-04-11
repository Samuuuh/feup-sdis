package channel;
import main.Peer;
import main.etc.Singleton;
import process.answer.PrepareStored;
import state.ChunkState;

import java.io.IOException;
import java.net.DatagramPacket;
import java.util.Timer;

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
                messageParsed = new MessageParser(packet.getData(), packet.getLength());

                if (messageParsed.getSenderId().equals(Peer.peer_no))
                    continue;


                if (messageParsed.getMessageType().equals(Singleton.PUTCHUNK)) {
                    if (Peer.peer_state.canPutFile(messageParsed.getData().length)) {
                        String chunkId = Singleton.getChunkId(messageParsed.getFileId(), messageParsed.getChunkNo());
                        updateState(messageParsed, chunkId);
                        Peer.reclaimBackupTasks.abortTask(chunkId);
                        scheduleStore(messageParsed, chunkId);
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void scheduleStore(MessageParser messageParsed, String chunkId){
        double delay = Math.random() * 400 + 1;
        
        Timer storeTimer = new Timer();
        storeTimer.schedule(new PrepareStored(messageParsed), (long) delay);
        Peer.storeTasks.addTask(chunkId, storeTimer);
    }

    private void updateState(MessageParser messageParsed, String chunkId){
        ChunkState chunkState = new ChunkState(chunkId, Integer.parseInt(messageParsed.getReplicationDeg()), messageParsed.getData().length/1000);
        Peer.peer_state.putChunk(chunkId, chunkState);
    }

}
