package send;

import factory.MessageBackup;
import file.Chunk;
import file.FileHandler;
import main.Definitions;
import main.Peer;
import send.SendMessageBackup;
import state.ChunkStatus;
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
            String fileId = Peer.hash(Definitions.getFilePath(Peer.peer_no));
            FileStatus fileStatus = new FileStatus(filePath, fileId, Integer.parseInt(replicationDeg));

            for (Chunk chunk : chunks) {
                // Add fileStatus to State.
                String chunkId = fileId + "-" + chunk.getChunkNo();
                fileStatus.addChunk(chunkId, 0);
                Peer.peer_state.putFile(fileId, fileStatus);

                new SendMessageBackup(filePath, "fileId", replicationDeg, chunk).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
