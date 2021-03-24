package process.postAnswer;

import channel.MessageParser;
import file.FileHandler;
import main.Definitions;
import main.Peer;
import send.SendChunkNo;
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
                System.out.println(entry.getKey());
                System.out.println(fileId);

                if (entry.getKey().matches(fileId + "-\\d+")) {
                    Peer.peer_state.removeChunk(entry.getKey());
                }
            }

            FileHandler.deleteChunks(fileId, "peers/peer_" + Peer.peer_no + "/chunks/");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
