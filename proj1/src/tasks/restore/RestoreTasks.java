package tasks.restore;

import main.etc.Logger;

import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stores the tasks of restore scheduled.
 */
public class RestoreTasks {

    /**
     * Key: chunkId that will be sent by the scheduled task.
     * Timer: Task that will perform the activity.
     */
    public static ConcurrentHashMap<String, Timer> restoreTasksHash = new ConcurrentHashMap<>();

    public static void addRestoreSchedule(String chunkId, Timer task) {
        restoreTasksHash.put(chunkId, task);
    }

    public static void abortRestoreSchedule(String chunkId) {
        try {
            Timer restoredTimer = restoreTasksHash.remove(chunkId);
            if (restoredTimer != null) {
                restoredTimer.cancel();
                Logger.ABORT_SEND("dataStructure.restore.RestoreTasks", "Abort sending chunk " + chunkId);
            }
        } catch (NullPointerException ignored) {
        }

    }

}
