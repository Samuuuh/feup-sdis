package process.request;

import main.Peer;
import main.etc.Chunk;
import main.etc.FileHandler;
import main.etc.Logger;
import main.etc.Singleton;
import send.SendPutChunk;
import state.FileState;
import tasks.backup.BackupCheck;
import tasks.backup.BackupTasks;
import tasks.backup.BackupTrackFile;

import java.io.IOException;
import java.util.Timer;

/**
 * Reads the file and send the chunks in multiCast.
 */
public class RequestPutChunk extends Thread {
    String filePath;
    String replicationDeg;

    public RequestPutChunk(String filePath, String replicationDeg) {
        this.filePath = filePath;
        this.replicationDeg = replicationDeg;
    }

    @Override
    public void run() {

        String fileId = Singleton.hash(filePath);
        try {
            byte[] fileContent = FileHandler.readFile(filePath);
            Chunk[] chunks = FileHandler.splitFile(fileContent);
            FileState fileState = new FileState(fileId, Integer.parseInt(replicationDeg));
            BackupTasks.addTrackFile(fileId, new BackupTrackFile(chunks.length));    // Track the file.

            for (Chunk chunk : chunks) {
                // Add fileStatus to State.
                String chunkId = fileId + "-" + chunk.getChunkNo();
                Logger.REQUEST(this.getClass().getName(), "Requested CHUNK No " + chunk.getChunkNo());
                fileState.addChunk(chunkId, 0);
                Peer.peer_state.putFile(fileId, fileState);

                new SendPutChunk(fileId, replicationDeg, chunk).start();

            }

            Logger.REQUEST(this.getClass().getName(), "Requested PUTCHUNK on " + fileId);
            scheduleBackupCheck(fileId);
            Logger.INFO(this.getClass().getName(), "Scheduled backup checking of file " + fileId);
        } catch (IOException e) {
            Logger.REQUEST(this.getClass().getName(), "Requested PUTCHUNK on " + fileId);
            e.printStackTrace();
        }

    }

    /**
     * Set task to check if the replication degree was achieved.
     */
    private void scheduleBackupCheck(String fileId){
        Timer timer = new Timer();
        timer.schedule(new BackupCheck(filePath, Integer.parseInt(replicationDeg)), 1000);
        BackupTasks.addBackupTask(fileId, timer);
    }



}
