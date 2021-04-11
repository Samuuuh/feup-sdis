package tasks;

import main.Peer;
import main.etc.Logger;
import state.ChunkState;

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
    private final ConcurrentHashMap<String, Timer> tasks = new ConcurrentHashMap<>();

    public void addTask(String chunkId, Timer task) {
        tasks.put(chunkId, task);
    }

    public void abortTask(String chunkId) {
        try {
            Timer restoredTimer = tasks.remove(chunkId);
            if (restoredTimer != null) {
                restoredTimer.cancel();
                Logger.ABORT_SEND(this.getClass().getName(), "Abort task " + chunkId);
            }
        } catch (NullPointerException ignored) {
        }

    }

    public void removeTask(String chunkId){
        tasks.remove(chunkId);
    }

    public Boolean isEmpty(){
        return tasks.isEmpty();
    }
}
