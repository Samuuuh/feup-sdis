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

    public RequestPutChunk(String chunkId, String replicationDeg, Integer currentTry) {
        this.chunkId = chunkId;
        this.replicationDeg = replicationDeg;
        this.currentTry = currentTry;
    }

    @Override
    public void run() {
        String fileId = Singleton.extractFileId(chunkId);
        int chunkNo = Integer.parseInt(Singleton.extractChunkNo(chunkId));

        try {
            String filePath = Peer.peer_state.getFileState(fileId).getFilePath();
            byte[] fileContent = FileHandler.readFile(filePath);
            assert fileContent != null;
            Chunk chunk = FileHandler.getChunk(fileContent, chunkNo);

            new SendPutChunk(fileId, replicationDeg, chunk).start();
            Logger.REQUEST(this.getClass().getName(), "Requested PUTCHUNK on " + chunkId);

            isFinalTry(filePath, fileId);

        } catch (IOException e) {
            Logger.REQUEST(this.getClass().getName(), "Requested PUTCHUNK on " + fileId);
            e.printStackTrace();
        }
    }

    private void isFinalTry(String filePath, String fileId) {
        if (currentTry < 5) {
            scheduleBackupCheck(filePath);
            Logger.INFO(this.getClass().getName(), "Scheduled backup checking of file " + fileId);
        }
    }

    /**
    Set task to check if the replication degree was achieved.
    */
    private void scheduleBackupCheck(String filePath) {
        Timer timer = new Timer();
        int delay = (int) Math.pow(2, currentTry);
        timer.schedule(new BackupChunkCheck(chunkId, currentTry + 1), delay* 1000L);
    }
}
