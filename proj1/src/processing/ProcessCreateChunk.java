package processing;

import file.Chunk;
import file.FileHandler;
import sendMessage.SendMessageBackup;

import java.io.IOException;

import static main.Peer.peer_no;

/**
 * Reads the file and send the chunks in multiCast.
 */
public class ProcessCreateChunk extends Thread {

    String replicationDeg;
    String filePath;

    public ProcessCreateChunk(String filePath, String replicationDeg) {
        this.filePath = filePath;
        this.replicationDeg = replicationDeg;
    }

    @Override
    public void run() {
        try {
            byte[] fileContent = FileHandler.readFile(filePath);
            Chunk[] chunks = FileHandler.splitFile(fileContent);

            for (int i = 0; i < chunks.length; i++) {

                new SendMessageBackup(filePath, "fileId", replicationDeg, chunks).start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
