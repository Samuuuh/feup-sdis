package tasks.backup;


import java.util.Timer;
import java.util.concurrent.ConcurrentHashMap;

/**
 * The system will schedule the thread necessary to check replication degree of a file.
 */
public class BackupTasks {

    /**
     * Key: fileId of the file being backed up.
     * Value: Task being scheduled.
     */
    public static ConcurrentHashMap<String, Timer> backupTasksHash = new ConcurrentHashMap<>();
    /**
     * Key: fileId.
     * Value: The class keeping track of the status of the backup process.
     */
    private static ConcurrentHashMap<String, BackupTrackFile> backupTrackFileHash = new ConcurrentHashMap<>();

    public static void addBackupTask(String fileId, Timer timer){
        backupTasksHash.put(fileId, timer);
    }
    public static void removeBackupTask(String fileId){
        backupTasksHash.remove(fileId);
    }


    // BACKUP TRACK FILE
    public static void addTrackFile(String fileId, BackupTrackFile track){
        if (getTrackFile(fileId) == null)
            backupTrackFileHash.put(fileId, track);
    }

    public static BackupTrackFile getTrackFile(String fileId){
        return backupTrackFileHash.get(fileId);
    }
    public static BackupTrackFile removeTrackFile(String fileId){
        return  backupTrackFileHash.remove(fileId);
    }

    public static void updateTrackFile(String fileId, String peerId, String chunkNo){
        BackupTrackFile trackFile = removeTrackFile(fileId);
        if (trackFile != null){
            trackFile.updateChunkRepDeg(chunkNo, peerId);
        }
        addTrackFile(fileId, trackFile);
    }

    /**
     * Increases how many tries that were to backup a file.
     * @param fileId
     */
    public static void increaseTries(String fileId){
        BackupTrackFile trackFile = removeTrackFile(fileId);
        if (trackFile != null)
            trackFile.increaseTries();
        addTrackFile(fileId, trackFile);
    }

}
