package process.request;

import main.etc.Chunk;
import tasks.backup.ReclaimChunkCheck;

import java.util.Timer;

public class RequestPutChunkReclaim extends RequestPutChunk{

    public RequestPutChunkReclaim(String chunkId, String replicationDeg, Integer currentTry, Chunk chunk) {
        super(chunkId, replicationDeg, currentTry);
        this.chunk = chunk;
    }

    protected void scheduleBackupCheck(){
        Timer timer = new Timer();
        int delay = (int) Math.pow(2, currentTry);
        timer.schedule(new ReclaimChunkCheck(chunkId, currentTry + 1, chunk), delay * 1000L);
    }
}
