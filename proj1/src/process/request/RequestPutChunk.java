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
    Integer currentTry = 0;

    public RequestPutChunk(String chunkId, String replicationDeg, Integer currentTry) {
        this.chunkId = chunkId;
        this.replicationDeg = replicationDeg;
        this.currentTry = currentTry;
    }


    @Override
    public void run() {

        String fileId = Singleton.extractFileId(chunkId);
        try {
            String filePath = Peer.peer_state.getFileState(fileId).getFilePath();
            byte[] fileContent = FileHandler.readFile(filePath);
            Chunk chunk = FileHandler.getChunk(fileContent, Singleton.extractChunkNo(chunkId));

            new SendPutChunk(fileId, replicationDeg, chunk).start();
            Logger.REQUEST(this.getClass().getName(), "Requested PUTCHUNK on " + chunkId);

            if (currentTry < 5){
                scheduleBackupCheck(filePath);
                Logger.INFO(this.getClass().getName(), "Scheduled backup checking of file " + fileId);
            }
        } catch (IOException e) {
            Logger.REQUEST(this.getClass().getName(), "Requested PUTCHUNK on " + fileId);
            e.printStackTrace();
        }

    }

    /**
     * Set task to check if the replication degree was achieved.
     */
    private void scheduleBackupCheck(String filePath) {
        Timer timer = new Timer();
        timer.schedule(new BackupChunkCheck(filePath, chunkId, currentTry + 1), 1000);
    }


}
