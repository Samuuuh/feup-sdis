package process;

import main.Peer;
import main.etc.FileHandler;
import state.ChunkState;

import java.io.IOException;

public class DeleteSingleChunk extends Thread {

    private final String chunkId;

    public DeleteSingleChunk(String chunkId){
        this.chunkId = chunkId;
    }

    @Override
    public void run(){
        try {
            ChunkState chunkState = Peer.peer_state.getChunkState(chunkId);
            Peer.peer_state.removeChunk(chunkId);
            FileHandler.deleteChunk(chunkId);

        } catch (IOException e) {}
    }
}
