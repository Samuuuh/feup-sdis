package process.request;

import main.Peer;
import main.etc.Chunk;
import main.etc.FileHandler;
import main.etc.Logger;
import tasks.backup.ChunkCheck;

import java.io.IOException;
import java.util.Timer;

public class RequestPutChunkBackup extends RequestPutChunk{

    String filePath;
    public RequestPutChunkBackup(String chunkId, String replicationDeg, Integer currentTry){
        super(chunkId, replicationDeg, currentTry);
        this.filePath = Peer.peer_state.getFileState(fileId).getFilePath();
        this.chunk = getChunkFromFile();
    }

    private Chunk getChunkFromFile() {
        try {
            byte[] fileContent = FileHandler.readFile(filePath);
            assert fileContent != null;
            return FileHandler.getChunk(fileContent, chunkNo);
        } catch (IOException e) {
            Logger.ERR(this.getClass().getName(), "Error on reading chunk " + fileId);
        }
        return null;
    }

    @Override
    protected void scheduleBackupCheck() {
        Timer timer = new Timer();
        int delay = (int) Math.pow(2, currentTry);
        timer.schedule(new ChunkCheck(chunkId, currentTry + 1), delay * 1000L);
    }
}
