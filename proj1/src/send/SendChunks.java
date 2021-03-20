package send;

import factory.MessageBackup;
import file.Chunk;
import file.FileHandler;
import send.SendMessageBackup;
import state.FileStatus;

import java.io.IOException;

/**
 * Reads the file and send the chunks in multiCast.
 */
public class SendChunks extends Thread {
    String filePath;
    String replicationDeg;

    public SendChunks(String filePath, String replicationDeg) {
        this.filePath = filePath;
        this.replicationDeg = replicationDeg;
    }

    @Override
    public void run() {
        try {
            byte[] fileContent = FileHandler.readFile(filePath);
            Chunk[] chunks = FileHandler.splitFile(fileContent);
            //FileStatus fileStatus = new FileStatus(filePath);

            for (Chunk chunk : chunks) {
                // TODO: add information in filehash.
                new SendMessageBackup(filePath, "fileId", replicationDeg, chunk).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
