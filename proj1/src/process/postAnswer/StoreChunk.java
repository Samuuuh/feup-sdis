package process.postAnswer;

import channel.MessageParser;
import file.FileHandler;
import main.Peer;
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
        Peer.increaseWaitingToRestore(messageParsed.getFileId());
        FileState fileState = Peer.peer_state.fileHash.get(messageParsed.getFileId());
        int chunkNo = fileState.getChunkStateHash().size();

        if (Peer.waitingToRestore.get(messageParsed.getFileId()) == chunkNo ) {
            try {
                FileHandler.saveFile(messageParsed.getFileId(), "peers/peer_" + Peer.peer_no + "/restore/", chunkNo - 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
