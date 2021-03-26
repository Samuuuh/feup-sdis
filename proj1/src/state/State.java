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
     * Removes the fileId from the fileHas.
     * @param key fileId.
     */
    public void removeFile(String key) {
        fileHash.remove(key);
    }

    public void removeChunk(String key) {
        chunkHash.remove(key);
    }

    public FileState getFileState(String key){
        return fileHash.get(key);
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

    /**
     * Updates a FileState by other.
     * @param fileId
     * @param newState New FileState to replace the old one.
     */
    public void updateFileState(String fileId, FileState newState){
        removeFile(fileId);
        putFile(fileId, newState);
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
