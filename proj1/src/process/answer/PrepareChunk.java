package process.answer;

import tasks.Tasks;
import main.etc.FileHandler;
import main.Peer;
import main.etc.Logger;
import main.etc.Singleton;
import send.SendChunk;

import java.io.File;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Prepare the information to send the CHUNK message.
 */
public class PrepareChunk extends Thread {
    private final String fileId;
    private final String chunkNo;

    public PrepareChunk(String fileId, String chunkNo) {
        this.fileId = fileId;
        this.chunkNo = chunkNo;
    }

    @Override
    public void run() {

        String chunkId = Singleton.getChunkId(fileId, chunkNo);
        String path = Singleton.getFilePath(Peer.peer_no) + chunkId;
        File file = new File(path);

        try {
            if (file.exists()) {
                byte[] body = FileHandler.readFile(path);
                scheduleSendMessage(fileId, body, chunkNo, chunkId);
                Logger.INFO(this.getClass().getName(), "SCHEDULED sending " + chunkId);
            }
        } catch (Exception e) {
            Logger.ERR(this.getClass().getName(), "Error restoring chunk " + chunkId);
        }
    }

    /**
     *  Will create Timer to schedule the operation.
     */
    private void scheduleSendMessage(String fileId, byte[] body, String chunkNo, String chunkId){
        Timer timer = new Timer();      // A new thread Timer will be created.
        timer.schedule(createTimerTask(fileId, body, chunkNo), new Random().nextInt(401));
        Peer.restoreTasks.addTask(chunkId, timer);
    }

    /**
     * This method will create the timerTask to be scheduled by the timer.
     */
    private TimerTask createTimerTask(String fileId, byte[] body, String chunkNo){
         return new TimerTask() {
            @Override
            public void run() {
                Logger.SUC(this.getClass().getName(), "Sent CHUNK, chunkNo: " + Singleton.getChunkId(fileId, chunkNo));
                new SendChunk(Singleton.CHUNK, fileId, body, chunkNo).start();
                this.cancel();      // Do not repeat.
            }
        };
    }
}
