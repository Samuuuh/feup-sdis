package process;

import main.Peer;
import main.etc.Chunk;
import main.etc.FileHandler;
import main.etc.Logger;
import main.etc.Singleton;
import process.request.RequestPutChunkReclaim;
import state.ChunkState;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class RemoveCheck extends Thread {
    String chunkId;
    String fileId;
    String senderId;
    String chunkNo;

    public RemoveCheck(String fileId, String chunkId, String senderId) {
        this.fileId = fileId;
        this.chunkId = chunkId;
        this.senderId = senderId;
        this.chunkNo = Singleton.extractChunkNo(chunkId);
    }

    @Override
    public void run() {
        Peer.peer_state.removePeerOfFileChunk(chunkId, senderId);
        Peer.peer_state.removePeerOfChunk(chunkId, senderId);
 
        ChunkState chunkState = Peer.peer_state.getChunkState(chunkId);

        if (chunkState != null && !chunkState.haveDesiredRepDeg()) {
            storedBackup(chunkState);
        }

    }


    private void storedBackup(ChunkState chunkState) {
        try {
            int replicationDegree = chunkState.getDesiredRepDeg();
            String chunkPath = Singleton.getFilePath(Peer.peer_no) + chunkId;
            Chunk chunk = new Chunk(chunkNo, FileHandler.readFile(chunkPath));
            scheduleBackup(String.valueOf(replicationDegree), chunk);
        }catch(Exception ignored){}
    }

    private void scheduleBackup(String replicationDegree, Chunk chunk) {
        Timer timer = new Timer();      // A new thread Timer will be created.
        timer.schedule(createTimerTask(replicationDegree, chunk), new Random().nextInt(401));
        Peer.reclaimBackupTasks.addTask(chunkId, timer);
        Logger.INFO(this.getClass().getName(), "Scheduled backup on chunk STORED\n");
    }


    private TimerTask createTimerTask(String replicationDegree, Chunk chunk) {
        return new TimerTask() {
            @Override
            public void run() {
                new RequestPutChunkReclaim(chunkId, replicationDegree, 0, chunk).start();
                this.cancel();      // Do not repeat.
            }
        };
    }

}
