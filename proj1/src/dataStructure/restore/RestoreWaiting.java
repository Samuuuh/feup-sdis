package dataStructure.restore;

import java.util.concurrent.ConcurrentHashMap;

/**
 * Stores the files that are being restored and number of chunks it has received so far.
 */
public class RestoreWaiting {

    /**
     * Key: The fileId.
     * Value: The number of chunks received so far.
     */
    public static ConcurrentHashMap<String, Integer> waitingToRestore = new ConcurrentHashMap<>();

    public static void addWaitingToRestore(String fileId) {
        waitingToRestore.put(fileId, 0);
    }

    public static void removeWaitingToRestore(String fileId) {
        waitingToRestore.remove(fileId);
    }

    public static Integer getWaitingToRestore(String fileId){
        return waitingToRestore.get(fileId);
    }

    public static Boolean isWaitingToRestore(String fileId) {
        return waitingToRestore.get(fileId) != null;
    }

    /**
     * Increases the number of chunks received by a file.
     * @param fileId Related file id.
     */
    public static void increaseWaitingToRestore(String fileId){
        int numReceived = waitingToRestore.remove(fileId);
        numReceived+= 1;
        waitingToRestore.put(fileId, numReceived);
    }

}
