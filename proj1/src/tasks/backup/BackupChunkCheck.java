package tasks.backup;

import main.Peer;
import main.etc.Logger;
import main.etc.Singleton;
import process.request.RequestFilePutChunk;
import process.request.RequestPutChunk;
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
public class BackupChunkCheck extends TimerTask {
    String fileId;
    Integer currentTry;
    String chunkId ;

    public BackupChunkCheck(String chunkId, Integer currentTry) {
        this.currentTry = currentTry;
        this.chunkId = chunkId;
        this.fileId = Singleton.extractFileId(chunkId);

    }

    @Override
    public void run() {
        Logger.INFO(this.getClass().getName(), "Backup check executing...");
        // For every chunk that didn't achieved desired replication degree, request again.
        FileState fileState =  Peer.peer_state.getFileState(fileId);
        ChunkState chunkState;
        if (fileState == null){
            chunkState = Peer.peer_state.getChunkState(chunkId);
        }else {
            chunkState = fileState.getChunkStateHash().get(chunkId);
        }

        if (!chunkState.haveDesiredRepDeg()) {
            Logger.INFO(this.getClass().getName(), "Try No. " + currentTry + " RESEND chunk " + chunkId);
            new RequestPutChunk(chunkId, String.valueOf(chunkState.getDesiredRepDeg()), currentTry).start();
        }else{
            Logger.SUC(this.getClass().getName(), "Chunk has achieved desired replication degree");
        }
    }

}
