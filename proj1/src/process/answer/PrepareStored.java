package process.answer;

import channel.MessageParser;
import main.Peer;
import main.etc.Logger;
import main.etc.Singleton;
import send.SendWithChunkNo;
import state.ChunkState;

import java.util.TimerTask;

import static main.etc.FileHandler.saveFileChunks;

/**
 * Saves the chunk and prepares to send the STORED message.
 */
public class PrepareStored extends TimerTask {
    MessageParser messageParsed;
    String chunkNo;
    String fileId;

    public PrepareStored(MessageParser messageParsed) {
        this.messageParsed = messageParsed;
        this.chunkNo = messageParsed.getChunkNo();
        this.fileId = messageParsed.getFileId();
    }

    @Override
    public void run() {
        String chunkId = Singleton.getChunkId(fileId, chunkNo);
        Peer.peer_state.updateChunkState(chunkId, Peer.peer_no);
        Boolean fileIsSaved = saveFileChunks(messageParsed, Singleton.getFilePath(Peer.peer_no));

        if (fileIsSaved) {
            new SendWithChunkNo(Singleton.STORED, fileId, chunkNo, Peer.mc_addr, Peer.mc_port).start();
            // After saving, update the perceived replication degree.
            Logger.INFO(this.getClass().getName(), "Sending STORED message on " + chunkId);
        } else {
            Logger.ERR(this.getClass().getName(), "Chunk " + Singleton.getChunkId(fileId, chunkNo) + "wasn't stored!");
        }
        Peer.reclaimBackupTasks.removeTask(chunkId);
    }
}
