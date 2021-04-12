package tasks.backup;

import main.Peer;
import main.etc.Logger;
import main.etc.Singleton;
import process.request.RequestFilePutChunk;
import process.request.RequestPutChunk;
import process.request.RequestPutChunkBackup;
import state.ChunkState;
import state.FileState;

import java.util.HashMap;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Checks if the backup has achieved the replication degree.
 * If yes, end the process.
 * Case not repeat try to send again the backup.
 */
public class BackupFileCheck extends TimerTask {
    String fileId;
    Integer currentTry;
    boolean backupFinished;
    public BackupFileCheck(String fileId, Integer currentTry) {
        this.fileId = fileId;
        this.currentTry = currentTry;
        this.backupFinished = true;
    }

    @Override
    public void run() {
        Logger.INFO(this.getClass().getName(), "Backup check executing...");

        // For every chunk that didn't achieved desired replication degree, request again.
        FileState fileState =  Peer.peer_state.getFileState(fileId);
        ConcurrentHashMap<String, ChunkState> chunksState = fileState.getChunkStateHash();
        chunksState.forEach((chunkId, chunkState)->{
            if (!chunkState.haveDesiredRepDeg()){
                Logger.INFO(this.getClass().getName(), "Try No. " + currentTry + "RESEND chunk " + chunkId);
                new RequestPutChunkBackup(chunkId, String.valueOf(chunkState.getDesiredRepDeg()), currentTry+1).start();
                backupFinished = false;
            }
        });

        if (backupFinished){
            Logger.SUC(this.getClass().getName(), "FINISHED backup file " + fileId);
        }



    }

}
