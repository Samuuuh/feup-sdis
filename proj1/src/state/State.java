package state;

import java.io.*;

import java.util.concurrent.ConcurrentHashMap;

public class State implements Serializable {
    public final String peer_no;

    private ConcurrentHashMap<String, FileStatus> fileHash;
    private ConcurrentHashMap<String, ChunkStatus> chunkHash;

    public State(String peer_no) {
        this.peer_no = peer_no;
    }


    public void putFile(String key, FileStatus fileStatus){
        fileHash.put(key, fileStatus);
    }

    public void putChunk(String key, ChunkStatus chunkStatus){
        chunkHash.put(key,chunkStatus);
    }

    public FileStatus getFile(String key){
        return fileHash.get(key);
    }

    public ChunkStatus getChunk(String key){
        return chunkHash.get(key);
    }
}
