package process.answer;

import file.FileHandler;
import main.Definitions;
import main.Peer;
import main.Utils;
import send.SendChunk;

import java.io.File;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;


public class PrepareChunk extends Thread {
    private final String fileId;
    private final String chunkNo;

    public PrepareChunk(String fileId, String chunkNo) {
        this.fileId = fileId;
        this.chunkNo = chunkNo;
    }


    @Override
    public void run() {
        try {
            String chunkId = Utils.buildChunkId(fileId, chunkNo);

            String path = Definitions.getFilePath(Peer.peer_no) + chunkId;
            File file = new File(path);

            if (file.exists()) {
                byte[] body = FileHandler.readFile(path);
                scheduleSendMessage(fileId, body, chunkNo, chunkId);
                System.out.println("Scheduled");
            }
        } catch (Exception e) {
            System.out.println("Error Restoring");
        }
    }

    /**
     *  Will create Timer to schedule the operation.
     */
    private void scheduleSendMessage(String fileId, byte[] body, String chunkNo, String chunkId){
        Timer timer = new Timer();      // A new thread Timer will be created.
        timer.schedule(createTimerTask(fileId, body, chunkNo), new Random().nextInt(401));
        Peer.addRestoreSchedule(chunkId, timer);
    }

    /**
     * This method will create the timerTask to be scheduled by the timer.
     */
    private TimerTask createTimerTask(String fileId, byte[] body, String chunkNo){
         return new TimerTask() {
            @Override
            public void run() {
                new SendChunk(Definitions.CHUNK, fileId, body, chunkNo).start();
                this.cancel();      // Do not repeat.
            }
        };
    }
}
