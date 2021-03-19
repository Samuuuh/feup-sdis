package state;

import file.Chunk;

import java.io.*;

import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class State implements Serializable {
    public final String peer_no;

    public ConcurrentHashMap<String, FileStatus> fileHash;
    public ConcurrentHashMap<String, ChunkStatus> chunkHash;

    public State(String peer_no) {
        this.peer_no = peer_no;
        fileHash = new ConcurrentHashMap<>();
        chunkHash = new ConcurrentHashMap<>();
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
        System.out.println(fileHash.size());
        System.out.println(fileHash.toString());
    }

}
