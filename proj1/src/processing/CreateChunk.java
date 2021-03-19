package processing;

import file.Chunk;
import file.FileHandler;
import send.SendMessageBackup;

import java.io.IOException;

/**
 * Reads the file and send the chunks in multiCast.
 */
public class CreateChunk extends Thread {
    String filePath;
    String replicationDeg;

    public CreateChunk(String filePath, String replicationDeg) {
        this.filePath = filePath;
        this.replicationDeg = replicationDeg;
    }

    @Override
    public void run() {
        try {
            byte[] fileContent = FileHandler.readFile(filePath);
            Chunk[] chunks = FileHandler.splitFile(fileContent);

            // TODO: FOR LOOP MUST BE HERE
            new SendMessageBackup(filePath, "fileId", replicationDeg, chunks).start();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
