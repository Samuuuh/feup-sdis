package process.answer;

import main.etc.FileHandler;
import main.Peer;
import main.etc.Logger;
import main.etc.Singleton;
import send.SendChunk;
import send.TCP.SendChunkTCP;

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
    private final String chunkId;
    public PrepareChunk(String chunkId) {
        this.chunkId = chunkId;
        this.chunkNo = Singleton.extractChunkNo(chunkId);
        this.fileId = Singleton.extractFileId(chunkId);
    }

    @Override
    public void run() {
        String path = Singleton.getFilePath(Peer.peer_no) + chunkId;
        File file = new File(path);

        try {
            if (file.exists()) {
                byte[] body = FileHandler.readFile(path);
                // TODO: VERSION HERE
                if(false) {
                    scheduleSendMessage(fileId, body, chunkNo, chunkId);
                } else {
                    scheduleSendMessageTCP(fileId, body, chunkNo, chunkId);
                }
                Logger.INFO(this.getClass().getName(), "Scheduled sending " + chunkId);
            }
        } catch (Exception e) {
            Logger.ERR(this.getClass().getName(), "Error restoring chunk " + chunkId);
        }
    }

    /**
     *  Will create Timer to schedule the operation.
     */
    private void scheduleSendMessage(String fileId, byte[] body, String chunkNo, String chunkId){
        Timer timer = new Timer();
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

    private void scheduleSendMessageTCP(String fileId, byte[] body, String chunkNo, String chunkId){
        Timer timer = new Timer();
        timer.schedule(createTimerTaskTCP(fileId, body, chunkNo), new Random().nextInt(401));
        Peer.restoreTasks.addTask(chunkId, timer);
    }

    private TimerTask createTimerTaskTCP(String fileId, byte[] body, String chunkNo){
        return new TimerTask() {
            @Override
            public void run() {
                Logger.SUC(this.getClass().getName(), "Sent CHUNK, chunkNo: " + Singleton.getChunkId(fileId, chunkNo));
                new SendChunkTCP(Singleton.CHUNK, fileId, body, chunkNo).start();
                this.cancel();      // Do not repeat.
            }
        };
    }
}
