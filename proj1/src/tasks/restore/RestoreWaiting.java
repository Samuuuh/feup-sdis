package tasks.restore;

import main.Peer;
import main.etc.Singleton;
import state.FileState;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Stores the files that are being restored and number of chunks it has received so far.
 */
public class RestoreWaiting {

    /**
     * Key: The chunkId.
     * Value: True if received, otherwise false.
     */
    public static ConcurrentHashMap<String, Boolean> waitingToRestore = new ConcurrentHashMap<>();
    /**
     * Key: fileID
     * Value: Number of chunks to restore.
     */
    public static ConcurrentHashMap<String, Integer> fileToRestore = new ConcurrentHashMap<>();

    public static void addWaitingToRestore(String chunkId) {
        waitingToRestore.put(chunkId, false);

        String fileId = Singleton.extractFileId(chunkId);
        FileState fileState = Peer.peer_state.getFileState(fileId);
        Integer numChunks = fileState.getChunkStateHash().size();
        fileToRestore.put(fileId, numChunks);

    }

    public static Integer numChunksToRestore(String fileId){
        return fileToRestore.get(fileId);
    }

    public static void removeWaitingToRestore(String fileId) {
        waitingToRestore.remove(fileId);

    }

    public static Boolean isWaitingToRestore(String chunkId) {
        if (waitingToRestore.get(chunkId) == null) return false;
        return !waitingToRestore.get(chunkId);
    }

    /**
     * Increases the number of chunks received by a file.
     * @param chunkId Related chunk Id.
     */
    public static void restoreReceived(String chunkId){
        waitingToRestore.put(chunkId, true);
        String fileId = Singleton.extractFileId(chunkId);
        decrementChunkNo(fileId);
    }

    private static void decrementChunkNo(String fileId){
        Integer numChunks = fileToRestore.get(fileId);
        fileToRestore.put(fileId, numChunks -1);
    }

    public static void removeFile(String fileId, int numChunks){
        fileToRestore.remove(fileId);

        for (int i = 0 ; i < numChunks; i++) {
            String chunkId = Singleton.getChunkId(fileId, String.valueOf(i));
            waitingToRestore.remove(chunkId);
        }
    }
}
