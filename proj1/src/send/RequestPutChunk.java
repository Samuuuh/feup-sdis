package send;

import file.Chunk;
import file.FileHandler;
import main.Peer;
import main.Utils;
import state.FileState;

import java.io.IOException;

/**
 * Reads the file and send the chunks in multiCast.
 */
public class RequestPutChunk extends Thread {
    String filePath;
    String replicationDeg;

    public RequestPutChunk(String filePath, String replicationDeg) {
        this.filePath = filePath;
        this.replicationDeg = replicationDeg;
    }

    @Override
    public void run() {
        try {
            byte[] fileContent = FileHandler.readFile(filePath);
            Chunk[] chunks = FileHandler.splitFile(fileContent);

            String fileId = Utils.hash(filePath);
            FileState fileState = new FileState(filePath, fileId, Integer.parseInt(replicationDeg));

            for (Chunk chunk : chunks) {
                // Add fileStatus to State.
                String chunkId = fileId + "-" + chunk.getChunkNo();
                fileState.addChunk(chunkId, 0);
                Peer.peer_state.putFile(fileId, fileState);

                new SendMessageBackup(filePath, "fileId", replicationDeg, chunk).start();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
