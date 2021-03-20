package state;

import file.Chunk;

import java.io.*;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class State implements Serializable {
    public final String peer_no;

    public ConcurrentHashMap<String, FileStatus> fileHash = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, ChunkStatus> chunkHash = new ConcurrentHashMap<>();

    public State(String peer_no) {
        this.peer_no = peer_no;
    }


    public void putFile(String key, FileStatus fileStatus) {
        fileHash.put(key, fileStatus);
    }

    public void putChunk(String key, ChunkStatus chunkStatus) {
        chunkHash.put(key, chunkStatus);
        System.out.println("INSIDE STATE");
        printState();
    }

    public FileStatus getFile(String key) {
        return fileHash.get(key);
    }

    public ChunkStatus getChunk(String key) {
        return chunkHash.get(key);
    }

    // Just to test
    public void printState() {
        System.out.println("I WAS CALLED");
        System.out.println(chunkHash.size());
        System.out.println(chunkHash.toString());
    }

}
