package tasks.backup;

import main.Peer;
import main.etc.Chunk;
import process.request.RequestPutChunkReclaim;
import state.ChunkState;

public class ReclaimChunkCheck extends ChunkCheck {
    Chunk chunk;
    public ReclaimChunkCheck(String chunkId, Integer currentTry, Chunk chunk){
        super(chunkId, currentTry);
        this.chunk = chunk;
    }

    @Override
    public void RequestPutChunk(ChunkState chunkState){
        new RequestPutChunkReclaim(chunkId, String.valueOf(chunkState.getDesiredRepDeg()), currentTry, chunk).start();
    }

    @Override
    public ChunkState getChunkState(){
        return Peer.peer_state.getChunkState(chunkId);
    }
}
