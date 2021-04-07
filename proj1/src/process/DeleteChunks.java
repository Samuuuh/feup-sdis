package process;

import main.Peer;
import main.etc.FileHandler;
import main.etc.Logger;
import state.ChunkState;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DeleteChunks extends Thread {
    private final String fileId;
    public DeleteChunks(String fileId) {
        this.fileId = fileId;
    }

    @Override
    public void run() {
        try {
            ConcurrentHashMap<String, ChunkState> chunksState = Peer.peer_state.chunkStored;
            if (chunksState == null) return;
            for (Map.Entry<String, ChunkState> chunkState : chunksState.entrySet()) {
                if (chunkState.getKey().matches(fileId + "-\\d+")) {
                    Peer.peer_state.removeChunk(chunkState.getKey());
                    FileHandler.deleteChunk(chunkState.getKey());
                }
            }
            Logger.SUC(this.getClass().getName(), "File " + fileId + " was deleted.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
