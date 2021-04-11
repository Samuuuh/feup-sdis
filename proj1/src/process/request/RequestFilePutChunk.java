package process.request;

import main.Peer;
import main.etc.Chunk;
import main.etc.FileHandler;
import main.etc.Logger;
import main.etc.Singleton;
import send.SendPutChunk;
import state.FileState;
import tasks.backup.BackupFileCheck;

import java.io.IOException;
import java.util.Timer;

/**
 * Reads the file and send the chunks in multiCast.
 */
public class RequestFilePutChunk extends Thread {
    String filePath;
    String replicationDeg;
    String fileId;

    public RequestFilePutChunk(String filePath, String replicationDeg) {
        this.filePath = filePath;
        this.replicationDeg = replicationDeg;
        this.fileId = Singleton.hash(filePath);
    }

    @Override
    public void run() {
        try {
            byte[] fileContent = FileHandler.readFile(filePath);
            assert fileContent != null;
            Chunk[] chunks = FileHandler.splitFile(fileContent);

            FileState fileState = new FileState(fileId, Integer.parseInt(replicationDeg), filePath);
            Peer.peer_state.putFile(fileId, fileState);

            for (Chunk chunk : chunks) {
                // Add fileStatus to State.
                String chunkId = Singleton.getChunkId(fileId, chunk.getChunkNo());
                Logger.REQUEST(this.getClass().getName(), "Requested CHUNK No " + chunk.getChunkNo());
                Peer.peer_state.getFileState(fileId).addChunk(chunkId, Integer.parseInt(replicationDeg));

                new SendPutChunk(fileId, replicationDeg, chunk).start();
            }

            Logger.REQUEST(this.getClass().getName(), "Finished requesting PUTCHUNK on " + fileId);
            scheduleBackupCheck();
            Logger.INFO(this.getClass().getName(), "Scheduled backup checking of file " + fileId);
        } catch (IOException e) {
            Logger.REQUEST(this.getClass().getName(), "Error on Requested PUTCHUNK on " + fileId);
            e.printStackTrace();
        }

    }

    /**
     * Set task to check if the replication degree was achieved.
     */
    private void scheduleBackupCheck(){
        Timer timer = new Timer();
        timer.schedule(new BackupFileCheck(fileId, 0), 1000);
    }



}
