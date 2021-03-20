package state;
import java.io.*;

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
        System.out.println("CHUNK HASH");
        System.out.println(chunkHash.size());
        System.out.println(chunkHash.toString());

        System.out.println("FILE HASH");
        System.out.println(fileHash.size());
        System.out.println(fileHash.toString());
    }

    /**
     *  Increases the replication degree of a chunkId.
     */
    public void increaseRepDeg(String fileId, String chunkId){
        System.out.println(fileId);
        System.out.println(chunkId);

        FileStatus fileStatus =  fileHash.remove(fileId);
        System.out.println("aqui");
        if (fileStatus == null) return;
        System.out.println("Teste");
        fileStatus.increaseChunkRepDeg(chunkId);
        fileHash.put(fileStatus.getFileId(), fileStatus);
        System.out.println(fileStatus.toString());
    }
}
