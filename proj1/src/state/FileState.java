package state;

import java.io.Serializable;
import java.util.concurrent.ConcurrentHashMap;

public class FileState implements Serializable {
    private final String fileId;
    private final int desiredRepDeg;

    public ConcurrentHashMap<String, ChunkState> chunkStateHash = new ConcurrentHashMap<>();

    public FileState(String fileId, int desiredRepDeg){
        this.fileId = fileId;
        this.desiredRepDeg = desiredRepDeg;
    }

    public ConcurrentHashMap<String, ChunkState> getChunkStateHash() {
        return chunkStateHash;
    }

    public String getFileId(){
        return fileId;
    }

    public void addChunk(String chunkId, int perceivedRepDeg) {
        ChunkState chunkState = new ChunkState(chunkId, perceivedRepDeg);
        chunkStateHash.put(chunkId, chunkState);
    }


    public String toString(){
        String s = "";
        s += "FILE STATUS\n";
        s += "REPDEG DESIRED " + desiredRepDeg + "\n";
        s += "FILEID = " + this.fileId + "\n";
        s += chunkStateHash.toString();

        return s;
    }
}
