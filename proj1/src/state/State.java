package state;

import main.etc.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class State implements Serializable {
    public static int totalSpace = 1000000;
    public static int occupiedSpace = 0;
    public final String peer_no;

    public ConcurrentHashMap<String, FileState> filesBackup = new ConcurrentHashMap<>();
    public ConcurrentHashMap<String, ChunkState> chunkStored = new ConcurrentHashMap<>();

    /**
     * Key: Peer
     * Value: List of FileIds.
     */
    public ConcurrentHashMap<String, List<String>> filesToDelete = new ConcurrentHashMap<>();

    public State(String peer_no) {
        this.peer_no = peer_no;
    }

    public void putFile(String key, FileState fileState) {
        filesBackup.put(key, fileState);
    }

    public void putChunk(String key, ChunkState chunkState) {
        ChunkState previousState = chunkStored.put(key, chunkState);
        // Increase the occupied size.
        if (previousState == null) {
            occupiedSpace += chunkState.getSize();
            Logger.INFO(this.getClass().getName(), "Current occupied space: " + occupiedSpace);
        }

    }

    public void removeFile(String key) {
        filesBackup.remove(key);
    }

    public void removeChunk(String key) {
        chunkStored.remove(key);
    }

    public void removeFileToDelete(String peer_no, String fileId){
        List<String> filesId = filesToDelete.get(peer_no);
        if (filesId != null){
            filesId.remove(fileId);
        }
    }

    public void removeDeletesOfPeer(String peer_no){
        filesToDelete.remove(peer_no);
    }

    public FileState getFileState(String key){
        return filesBackup.get(key);
    }

    public ChunkState getChunkState(String chunkId){
        return chunkStored.get(chunkId);
    }

    public Set<String> getChunkKeys(){
        return chunkStored.keySet();
    }

    /**
     *  Updates the replication degree of a chunkId.
     */
    public void updateChunkState(String chunkId, String peer) {
        ChunkState chunkState = chunkStored.get(chunkId);
        if (chunkState != null){
            chunkState.addStoredPeer(peer);
        }
    }

    /*
     * Update replication degree of a chunk
     */
    public void updateFileState(String fileId, String chunkNo, String peer) {
        FileState fileState = filesBackup.get(fileId);
        if (fileId != null && fileState != null ){
            fileState.getChunkState(chunkNo).addStoredPeer(peer);
        }
    }

    public void addFileToDelete(String peer_no, String fileId){
        List<String> filesId = filesToDelete.get(peer_no);
        if (filesId == null){
            List<String> newFilesId = new ArrayList<>();
            newFilesId.add(fileId);
            filesToDelete.put(peer_no, newFilesId);
        }
        else filesId.add(fileId);
    }



    public List<String> getFilesToDelete(String peer_no){
        return filesToDelete.get(peer_no);
    }

    // Just to test
    public void printState() {
        /*System.out.println("CHUNK HASH");
        System.out.println(chunkStored.size());
        System.out.println(chunkStored.toString());

        System.out.println("FILE HASH");
        System.out.println(filesBackup.size());
        System.out.println(filesBackup.toString());*/

        System.out.println(filesToDelete);
    }
}
