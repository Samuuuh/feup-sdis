package process.request;

import main.Peer;
import main.etc.Chunk;
import main.etc.FileHandler;
import main.etc.Logger;
import main.etc.Singleton;
import send.SendPutChunk;
import tasks.backup.BackupChunkCheck;
import tasks.backup.BackupFileCheck;

import java.io.IOException;
import java.util.Timer;

/**
 * Reads the file and send the chunks in multiCast.
 */
public class RequestPutChunk extends Thread {
    String chunkId;
    String replicationDeg;
    Integer currentTry;
    Chunk chunk;
    String fileId;
    String filePath;
    int chunkNo;

    public RequestPutChunk(String chunkId, String replicationDeg, Integer currentTry) {
        this.chunkId = chunkId;
        this.replicationDeg = replicationDeg;
        this.currentTry = currentTry;
        this.fileId = Singleton.extractFileId(chunkId);
        this.filePath = Peer.peer_state.getFileState(fileId).getFilePath();
        this.chunkNo = Integer.parseInt(Singleton.extractChunkNo(chunkId));
        this.chunk = getChunkFromFile();
    }

    public RequestPutChunk(String chunkId, String replicationDeg, Integer currentTry, Chunk chunk) {
        this.chunkId = chunkId;
        this.replicationDeg = replicationDeg;
        this.currentTry = currentTry;
        this.fileId = Singleton.extractFileId(chunkId);
        this.filePath = Singleton.getFilePath(Peer.peer_no) + chunkId;
        this.chunkNo = Integer.parseInt(Singleton.extractChunkNo(chunkId));
        this.chunk = chunk;
    }

    @Override
    public void run() {

        new SendPutChunk(fileId, replicationDeg, chunk).start();
        Logger.REQUEST(this.getClass().getName(), "Requested PUTCHUNK on " + chunkId);

        isFinalTry(fileId);
    }

    private Chunk getChunkFromFile() {
        try {
            byte[] fileContent = FileHandler.readFile(filePath);
            assert fileContent != null;
            return FileHandler.getChunk(fileContent, chunkNo);
        } catch (IOException e) {
            Logger.REQUEST(this.getClass().getName(), "Requested PUTCHUNK on " + fileId);
        }
        return null;
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
    private void scheduleBackupCheck() {
        Timer timer = new Timer();
        int delay = (int) Math.pow(2, currentTry);
        timer.schedule(new BackupChunkCheck(chunkId, currentTry + 1), delay * 1000L);
    }
}
