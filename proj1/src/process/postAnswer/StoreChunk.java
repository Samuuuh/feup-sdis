package process.postAnswer;

import channel.MessageParser;
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
        RestoreWaiting.increaseWaitingToRestore(messageParsed.getFileId());
        FileState fileState = Peer.peer_state.fileHash.get(messageParsed.getFileId());
        int chunkNo = fileState.getChunkStateHash().size();

        // TODO: change name of waiting to restore.
        if (RestoreWaiting.getWaitingToRestore(messageParsed.getFileId()) == chunkNo ) {
            try {
                FileHandler.saveFile(messageParsed.getFileId(), "peers/peer_" + Peer.peer_no + "/restore/", chunkNo - 1);
                Logger.SUC(this.getClass().getName(), "File " + messageParsed.getFileId() + " has been restored.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
