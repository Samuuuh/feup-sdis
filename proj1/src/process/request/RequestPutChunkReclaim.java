package process.request;

import main.Peer;
import main.etc.Chunk;
import main.etc.Singleton;
import send.SendWithChunkNo;
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
        // Says that it has stored the chunk.
        String chunkNo = Singleton.extractChunkNo(chunkId);
        new SendWithChunkNo(Singleton.STORED, fileId, chunkNo, Peer.mc_addr, Peer.mc_port).start();
    }
}
