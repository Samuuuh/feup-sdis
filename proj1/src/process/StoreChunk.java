package process;

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
    private final MessageParser messageParsed;
    private final String chunkNo;
    private final String fileId;

    public StoreChunk(MessageParser messageParsed) {
        this.fileId = messageParsed.getFileId();
        this.chunkNo = messageParsed.getChunkNo();
        this.messageParsed = messageParsed;
    }

    @Override
    public void run() {
        FileHandler.saveFileChunks(messageParsed, "peers/peer_" + Peer.peer_no + "/restore/");
        String chunkId = Singleton.getChunkId(fileId,chunkNo);
        RestoreWaiting.restoreReceived(chunkId);

        if (RestoreWaiting.numChunksToRestore(fileId) == 0 ) {
            try {
                Thread.sleep(400);
            }catch (Exception e){}
            joinChunks();
        }
    }

    private void joinChunks() {
        try {
            int numChunks = Peer.peer_state.getFileState(fileId).getChunkStateHash().size();

            FileHandler.saveFile(fileId, "peers/peer_" + Peer.peer_no + "/restore/", numChunks);
            RestoreWaiting.removeFile(fileId, numChunks);
            Logger.SUC(this.getClass().getName(), "File " + fileId + " has been restored.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
