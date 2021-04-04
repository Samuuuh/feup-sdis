package tasks;

import main.etc.Logger;

import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stores the tasks of restore scheduled.
 */
public class Tasks {

    /**
     * Key: chunkId that will be sent by the scheduled task.
     * Timer: Task that will perform the activity.
     */
    public ConcurrentHashMap<String, Timer> tasks = new ConcurrentHashMap<>();

    public void addTask(String chunkId, Timer task) {
        tasks.put(chunkId, task);
    }

    public void abortTask(String chunkId) {
        try {
            Timer restoredTimer = tasks.remove(chunkId);
            if (restoredTimer != null) {
                restoredTimer.cancel();
                Logger.ABORT_SEND("dataStructure.restore.RestoreTasks", "Abort task " + chunkId);
            }
        } catch (NullPointerException ignored) {
        }

    }

}
