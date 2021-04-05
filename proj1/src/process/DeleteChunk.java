package process;

import main.Peer;
import main.etc.FileHandler;
import main.etc.Logger;
import state.ChunkState;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DeleteChunk extends Thread {
    private final String fileId;
    public DeleteChunk(String fileId) {
        this.fileId = fileId;
    }

    @Override
    public void run() {
        try {
            deleteMatchChunks();

            FileHandler.deleteChunks(fileId, "peers/peer_" + Peer.peer_no + "/chunks/");
            //if (chunkHash.size() != 0)
            Logger.SUC(this.getClass().getName(), "File " + fileId + " was deleted.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void deleteMatchChunks() {
        ConcurrentHashMap<String, ChunkState> chunksState = Peer.peer_state.chunkStored;

        for (Map.Entry<String, ChunkState> chunkState : chunksState.entrySet()) {
            if (chunkState.getKey().matches(fileId + "-\\d+"))
                Peer.peer_state.removeChunk(chunkState.getKey());
        }
    }
}
