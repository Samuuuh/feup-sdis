package process;

import main.Peer;
import main.etc.Chunk;
import main.etc.FileHandler;
import main.etc.Logger;
import main.etc.Singleton;
import process.request.RequestPutChunk;
import state.ChunkState;
import state.FileState;

import java.io.IOException;
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
        try {
            ChunkState chunkState = Peer.peer_state.getChunkState(chunkId);
            chunkState.removePeer(senderId);

            if (!chunkState.haveDesiredRepDeg()) {
                storedBackup(chunkState);
            } else initiatorBackup();
        }catch(Exception e){}
    }


    private void storedBackup(ChunkState chunkState) throws IOException {

        int replicationDegree = chunkState.getDesiredRepDeg();
        String chunkPath = Singleton.getFilePath(Peer.peer_no) + chunkId;
        Chunk chunk = new Chunk(chunkNo, FileHandler.readFile(chunkPath));
        if (chunk.getChunkData() == null) return;
        scheduleBackup(String.valueOf(replicationDegree), chunk);
    }

    private void initiatorBackup() {
        FileState fileState = Peer.peer_state.getFileState(fileId);
        ChunkState chunkState = fileState.getChunkState(chunkNo);

        chunkState.removePeer(senderId);

        if (!chunkState.haveDesiredRepDeg()) {
            String replicationDegree = String.valueOf(chunkState.getDesiredRepDeg());
            scheduleBackup(replicationDegree);
        }
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
                new RequestPutChunk(chunkId, replicationDegree, 0, chunk).start();
                this.cancel();      // Do not repeat.
            }
        };
    }

    private void scheduleBackup(String replicationDegree) {
        Timer timer = new Timer();      // A new thread Timer will be created.
        timer.schedule(createTimerTask(replicationDegree), new Random().nextInt(401));
        Peer.reclaimBackupTasks.addTask(chunkId, timer);
        Logger.INFO(this.getClass().getName(), "Scheduled backup on file from INITIATOR\n");
    }


    private TimerTask createTimerTask(String replicationDegree) {
        return new TimerTask() {
            @Override
            public void run() {
                new RequestPutChunk(chunkId, replicationDegree, 0).start();
                this.cancel();      // Do not repeat.
            }
        };
    }
}
