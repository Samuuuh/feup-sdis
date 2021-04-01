package process.postAnswer;

import main.Peer;
import process.request.RequestPutChunk;
import state.ChunkState;
import state.FileState;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class RemoveCheck extends Thread{
    String chunkId;
    String fileId;
    String senderId;

    public RemoveCheck(String fileId, String chunkId, String senderId){
        this.fileId = fileId;
        this.chunkId = chunkId;
        this.senderId = senderId;
    }

    @Override
    public void run(){
        FileState fileState = Peer.peer_state.getFileState(fileId);
        ChunkState chunkState = fileState.getChunkState(chunkId);
        String replicationDegree = String.valueOf(chunkState.getDesiredRepDeg());
        chunkState.removePeer(senderId);
        if (!chunkState.haveDesiredRepDeg()){
            scheduleBackup(replicationDegree);
        }
    }


    /**
     *  Will create Timer to schedule the operation.
     */
    private void scheduleBackup(String replicationDegree){
        Timer timer = new Timer();      // A new thread Timer will be created.
        timer.schedule(createTimerTask(replicationDegree), new Random().nextInt(401));
        Peer.reclaimBackupTasks.addTask(chunkId, timer);
    }

    /**
     * This method will create the timerTask to be scheduled by the timer.
     */
    private TimerTask createTimerTask(String replicationDegree){
        return new TimerTask() {
            @Override
            public void run() {
                new RequestPutChunk(chunkId, replicationDegree, 0).start();
                this.cancel();      // Do not repeat.
            }
        };
    }
}
