package state;

import main.etc.Singleton;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class FileState implements Serializable {
    private final String fileId;
    private final int desiredRepDeg;
    private final String filePath;

    /**
     * Key: ChunkId
     */
    private final ConcurrentHashMap<String, ChunkState> chunkStateHash = new ConcurrentHashMap<>();

    public FileState(String fileId, int desiredRepDeg, String filePath) {
        this.fileId = fileId;
        this.desiredRepDeg = desiredRepDeg;
        this.filePath = filePath;
    }

    public ConcurrentHashMap<String, ChunkState> getChunkStateHash() {
        return chunkStateHash;
    }

    public String getFileId() {
        return fileId;
    }

    public String getFilePath(){
        return filePath;
    }

    public void addChunk(String chunkId, int desiredRepDeg) {
        ChunkState chunkState = new ChunkState(chunkId, desiredRepDeg);
        chunkStateHash.put(chunkId, chunkState);
    }

    public ChunkState getChunkState(String chunkNo) {
        String chunkId = Singleton.getChunkId(fileId, chunkNo);
        return chunkStateHash.get(chunkId);
    }

    public String toString() {
        String s = "";
        s += "FILE STATUS\n";
        s += "REPDEG DESIRED " + desiredRepDeg + "\n";
        s += "FILEID = " + this.fileId + "\n";
        s += chunkStateHash.toString();

        return s;
    }
}
