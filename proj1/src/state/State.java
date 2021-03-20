package state;
import java.io.*;

import java.util.concurrent.ConcurrentHashMap;

public class State implements Serializable {
    public final String peer_no;

    public ConcurrentHashMap<String, FileState> fileHash = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, ChunkState> chunkHash = new ConcurrentHashMap<>();

    public State(String peer_no) {
        this.peer_no = peer_no;
    }

    public void putFile(String key, FileState fileState) {
        fileHash.put(key, fileState);
    }

    public void putChunk(String key, ChunkState chunkState) {
        chunkHash.put(key, chunkState);
    }

    /**
     *  Increases the replication degree of a chunkId.
     */
    public void increaseRepDeg(String fileId, String chunkId){
        FileState fileState =  fileHash.remove(fileId);
        if (fileState == null) return;

        fileState.increaseChunkRepDeg(chunkId);
        fileHash.put(fileState.getFileId(), fileState);
    }

    // Just to test
    public void printState() {
        System.out.println("CHUNK HASH");
        System.out.println(chunkHash.size());
        System.out.println(chunkHash.toString());

        System.out.println("FILE HASH");
        System.out.println(fileHash.size());
        System.out.println(fileHash.toString());
    }
}
