package state;

import main.Peer;
import main.etc.Logger;
import main.etc.Singleton;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class State implements Serializable {
    public int totalSpace = 1000000;
    public int occupiedSpace = 0;
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

    public Boolean canPutFile(Integer chunkSize) {
        return totalSpace > occupiedSpace + chunkSize;
    }

    public void putFile(String key, FileState fileState) {
        filesBackup.put(key, fileState);
    }

    public void putChunk(String key, ChunkState chunkState) {
        if (chunkStored.get(key) != null) return;
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
        ChunkState chunkState = chunkStored.remove(key);
        occupiedSpace -= chunkState.getSize();
    }

    public void removePeerOfFileChunk(String chunkId, String peer_no){
        String fileId = Singleton.extractFileId(chunkId);
        String chunkNo = String.valueOf(Singleton.extractChunkNo(chunkId));
        FileState fileState = getFileState(fileId);

        if (fileState == null) return;

        ChunkState chunkState = fileState.getChunkState(chunkNo);
        if (chunkState == null) return;

        chunkState.removePeer(peer_no);

    }
    public void removePeerOfChunk(String chunkId, String peer_no){
        ChunkState chunkState = chunkStored.get(chunkId);
        if (chunkState == null) return;
        chunkState.removePeer(peer_no);
    }

    public void removeChunkFromFileState(String fileId, String chunkId) {
        FileState fileState = getFileState(fileId);
        if (fileState != null) {
            fileState.removeChunkState(chunkId);
        }
    }

    public void removeFileToDelete(String peer_no, String fileId) {
        List<String> filesId = filesToDelete.get(peer_no);
        if (filesId != null) {
            filesId.remove(fileId);
        }
    }

    public void removeDeletesOfPeer(String peer_no) {
        filesToDelete.remove(peer_no);
    }

    public FileState getFileState(String key) {
        return filesBackup.get(key);
    }

    public ChunkState getChunkState(String chunkId) {
        return chunkStored.get(chunkId);
    }

    public Set<String> getChunkKeys() {
        return chunkStored.keySet();
    }

    /**
     * Updates the replication degree of a chunkId.
     */
    public void updateChunkState(String chunkId, String peer) {
        ChunkState chunkState = chunkStored.get(chunkId);
        if (chunkState != null) {
            chunkState.addStoredPeer(peer);
        }
    }

    /*
     * Update replication degree of a chunk
     */
    public void updateFileState(String fileId, String chunkNo, String peer) {
        FileState fileState = filesBackup.get(fileId);
        if (fileId != null && fileState != null) {
            fileState.getChunkState(chunkNo).addStoredPeer(peer);
        }
    }

    public void addFileToDelete(String peer_no, String fileId) {
        List<String> filesId = filesToDelete.get(peer_no);
        if (filesId == null) {
            List<String> newFilesId = new ArrayList<>();
            newFilesId.add(fileId);
            filesToDelete.put(peer_no, newFilesId);
        } else filesId.add(fileId);
    }


    public List<String> getFilesToDelete(String peer_no) {
        return filesToDelete.get(peer_no);
    }

    public void saveState() throws IOException {
        String pathString = Singleton.getStatePath(Peer.peer_no);
        String filePathString = pathString + Singleton.STATE_FILE_NAME;

        Path path = Paths.get(pathString);
        Files.createDirectories(path);

        FileOutputStream fileOutputStream = new FileOutputStream(filePathString);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(Peer.peer_state);
        objectOutputStream.close();
        fileOutputStream.close();
    }
    @Override
    public String toString() {

        StringBuilder s = new StringBuilder("Files that peer " + peer_no + " has initiated backup: \n");
        Set<String> setOfFileIds = filesBackup.keySet();

        if (setOfFileIds.size() == 0)  s.append("--------\n\n");
            for (String fileId : setOfFileIds)
            s.append(getFileState(fileId));

        Set<String> setOfChunkIds = chunkStored.keySet();
        s.append("Chunk that peer ").append(peer_no).append(" has stored: \n");

        if (setOfChunkIds.size() == 0) s.append("--------\n\n");
        for (String chunkId : setOfChunkIds)
            s.append(chunkStored.get(chunkId));

        s.append("TOTAL SPACE ").append(totalSpace).append(" KB\n");
        s.append("OCCUPIED SPACE: ").append(occupiedSpace).append(" KB\n");

        return s.toString();
    }
}

