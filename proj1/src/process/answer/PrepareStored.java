package process.answer;

import channel.MessageParser;
import main.Peer;
import main.etc.Logger;
import main.etc.Singleton;
import send.SendChunkNo;
import state.ChunkState;

import static main.etc.FileHandler.saveFileChunks;

/**
 * Saves the chunk and prepares to send the STORED message.
 */
public class PrepareStored extends Thread {
    MessageParser messageParsed;

    public PrepareStored(MessageParser messageParsed) {
        this.messageParsed = messageParsed;
    }

    @Override
    public void run() {
        Boolean fileIsSaved = saveFileChunks(messageParsed, Singleton.getFilePath(Peer.peer_no));
        addChunkStatus();

        if (fileIsSaved) {
            new SendChunkNo(Singleton.STORED, messageParsed.getFileId(), messageParsed.getChunkNo(), Peer.mc_addr, Peer.mc_port).start();
            String chunkId = Singleton.buildChunkId(messageParsed.getFileId(), messageParsed.getChunkNo());
            Logger.INFO(this.getClass().getName(), "Sending STORED message on " + chunkId);
        } else {
            Logger.ERR(this.getClass().getName(), "Chunk " + Singleton.buildChunkId(messageParsed.getFileId(), messageParsed.getChunkNo()) + "wasn't stored!");
        }
    }

    /**
     * Once the chunk is stored. It's saved on the status.
     */
    public void addChunkStatus() {
        String id = messageParsed.getFileId() + "-" + messageParsed.getChunkNo();
        int size = messageParsed.getData().length;
        int repDeg = Integer.parseInt(messageParsed.getReplicationDeg());

        ChunkState chunkState = new ChunkState(id, size, repDeg);
        Peer.peer_state.putChunk(id, chunkState);
    }
}
