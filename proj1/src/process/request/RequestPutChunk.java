package process.request;

import dataStructure.Chunk;
import main.Peer;
import main.etc.FileHandler;
import main.etc.Logger;
import main.etc.Singleton;
import send.SendPutChunk;
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

        String fileId = Singleton.hash(filePath);
        try {
            byte[] fileContent = FileHandler.readFile(filePath);
            Chunk[] chunks = FileHandler.splitFile(fileContent);
            FileState fileState = new FileState(filePath, fileId, Integer.parseInt(replicationDeg));

            for (Chunk chunk : chunks) {
                // Add fileStatus to State.
                String chunkId = fileId + "-" + chunk.getChunkNo();
                fileState.addChunk(chunkId, 0);
                Peer.peer_state.putFile(fileId, fileState);

                new SendPutChunk(fileId, replicationDeg, chunk).start();
            }
            Logger.REQUEST(this.getClass().getName(), "Requested PUTCHUNK on " + fileId);

        } catch (IOException e) {
            Logger.REQUEST(this.getClass().getName(), "Requested PUTCHUNK on " + fileId);
            e.printStackTrace();
        }

    }


}
