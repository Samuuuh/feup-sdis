package process.postAnswer;

import main.Peer;
import main.etc.FileHandler;
import main.etc.Logger;
import state.ChunkState;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DeleteChunk extends Thread {
    private String fileId;
    public DeleteChunk(String fileId) {
        this.fileId = fileId;
    }

    @Override
    public void run() {
        try {
            ConcurrentHashMap<String, ChunkState> chunkHash = Peer.peer_state.chunkHash;

            for (Map.Entry<String, ChunkState> entry : chunkHash.entrySet()) {
                if (entry.getKey().matches(fileId + "-\\d+")) {
                    Peer.peer_state.removeChunk(entry.getKey());
                }
            }
            FileHandler.deleteChunks(fileId, "peers/peer_" + Peer.peer_no + "/chunks/");
            if (chunkHash.size() != 0 )
                Logger.SUC(this.getClass().getName(), "File " + fileId + " was deleted.");
            
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
