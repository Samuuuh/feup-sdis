package state;
import file.Chunk;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class FileStatus implements Status{
    private final String pathName;
    private final String fileId;
    private final int desiredRepDeg;
    public ConcurrentHashMap<String, ChunkStatus> chunkStateHash = new ConcurrentHashMap<>();

    public FileStatus(String pathName, String fileId, int desiredRepDeg){
        this.pathName = pathName;
        this.fileId = fileId;
        this.desiredRepDeg = desiredRepDeg;
    }

    public void addChunkList(List<Chunk> chunksList){

    }

    public String getFileId(){
        return fileId;
    }

    public void addChunk(String chunkId, int perceivedRepDeg) {

        ChunkStatus chunkStatus = new ChunkStatus(chunkId, perceivedRepDeg);
        chunkStateHash.put(chunkId, chunkStatus);
    }

    public void increaseChunkRepDeg(String chunkId){
        ChunkStatus chunkStatus = chunkStateHash.remove(chunkId);
        chunkStatus.setPerceivedRepDeg(chunkStatus.getPerceivedRepDeg() + 1 );
        chunkStateHash.put(chunkId, chunkStatus);
    }

    public String toString(){
        String s = "";
        s += "FILE STATUS";
        s += this.pathName;
        s += this.fileId;
        s += chunkStateHash.toString();

        return s;
    }
}
