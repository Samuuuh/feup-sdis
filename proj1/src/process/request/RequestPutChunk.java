package process.request;

import main.Peer;
import main.etc.Chunk;
import main.etc.Logger;
import main.etc.Singleton;
import send.SendPutChunk;

/**
 * Reads the file and send the chunks in multiCast.
 */
abstract public class RequestPutChunk extends Thread {
    String chunkId;
    String replicationDeg;
    Integer currentTry;
    Chunk chunk;
    String fileId;
    int chunkNo;

    public RequestPutChunk(String chunkId, String replicationDeg, Integer currentTry) {
        this.chunkId = chunkId;
        this.replicationDeg = replicationDeg;
        this.currentTry = currentTry;
        this.fileId = Singleton.extractFileId(chunkId);
        this.chunkNo = Integer.parseInt(Singleton.extractChunkNo(chunkId));
    }


    @Override
    public void run() {
        new SendPutChunk(fileId, replicationDeg, chunk).start();
        Logger.REQUEST(this.getClass().getName(), "Requested PUTCHUNK on " + chunkId);

        isFinalTry(fileId);
    }


    private void isFinalTry(String fileId) {
        if (currentTry < 5) {
            scheduleBackupCheck();
            Logger.INFO(this.getClass().getName(), "Scheduled backup checking of file " + fileId);
        }
    }

    /**
     * Set task to check if the replication degree was achieved.
     */
    protected abstract void scheduleBackupCheck();


}
