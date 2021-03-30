package state;

import main.etc.Logger;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class State implements Serializable {
    public static int totalSpace = 1000000;
    public static int occupiedSpace = 0;
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
        ChunkState previousState = chunkHash.put(key, chunkState);
        // Increase the occupied size.
        if (previousState == null) {
            occupiedSpace += chunkState.getSize();
            Logger.INFO(this.getClass().getName(), "Current occupied space: " + occupiedSpace);
        }

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

    public Set<String> getChunkKeys(){
        return chunkHash.keySet();
    }

    public ChunkState getChunkState(String key){
        return chunkHash.get(key);
    }


    /**
     *  Updates the replication degree of a chunkId.
     */
    public void addStoredPeer(String chunkId, String peer){
        ChunkState chunkState = chunkHash.remove(chunkId);
        if (chunkState != null){
            chunkState.addStoredPeer(peer);
            chunkHash.put(chunkId, chunkState);
        }
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
