package process.postAnswer;

import channel.MessageParser;
import main.etc.Singleton;
import tasks.restore.RestoreWaiting;
import main.etc.FileHandler;
import main.Peer;
import main.etc.Logger;
import state.FileState;

import java.io.IOException;

/**
 * Store the chunk after the CHUNK answer.
 */
public class StoreChunk extends Thread {
    private MessageParser messageParsed;

    public StoreChunk(MessageParser messageParsed) {
        this.messageParsed = messageParsed;
    }

    @Override
    public void run() {
        FileHandler.saveFileChunks(messageParsed, "peers/peer_" + Peer.peer_no + "/restore/");
        String chunkId = Singleton.getChunkId(messageParsed.getFileId(), messageParsed.getChunkNo());
        RestoreWaiting.restoreReceived(chunkId);
        int chunkNo = Peer.peer_state.getFileState(messageParsed.getFileId()).chunkStateHash.size();

        // TODO: change name of waiting to restore.
        if (RestoreWaiting.numChunksToRestore(messageParsed.getFileId()) == 0) {
            try {
                FileHandler.saveFile(messageParsed.getFileId(), "peers/peer_" + Peer.peer_no + "/restore/", chunkNo);
                RestoreWaiting.removeFile(messageParsed.getFileId(), chunkNo);
                Logger.SUC(this.getClass().getName(), "File " + messageParsed.getFileId() + " has been restored.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
