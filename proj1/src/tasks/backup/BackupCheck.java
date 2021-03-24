package tasks.backup;

import main.etc.Logger;
import main.etc.Singleton;
import process.request.RequestPutChunk;

import java.util.TimerTask;

/**
 * Checks if the backup has achieved the replication degree.
 * If yes, end the process.
 * Case not repeat try to send again the backup.
 */
public class BackupCheck extends TimerTask {
    String fileId;
    String filePath;
    Integer desiredRepDeg;
    public BackupCheck(String filePath, Integer desiredRepDeg){
        this.fileId = Singleton.hash(filePath);
        this.desiredRepDeg = desiredRepDeg;
        this.filePath = filePath;
    }

    @Override
    public void run(){
        Logger.INFO(this.getClass().getName(), "Backup check executing...");
        BackupTrackFile trackFile = BackupTasks.getTrackFile(fileId);
        if (trackFile != null){
            Integer repDeg = trackFile.getActualRepDeg();
            if (!this.desiredRepDeg.equals(repDeg) && !trackFile.achievedMaxTries()){
                Logger.INFO(this.getClass().getName(), "Backup not achieved desired replication degree on file " + fileId);
                Logger.ANY(this.getClass().getName(), "Backup RETRY no. " + BackupTasks.getTrackFile(fileId).getNumTries() +" on file " + fileId);

                BackupTasks.increaseTries(fileId);
                new RequestPutChunk(filePath, String.valueOf(desiredRepDeg)).start();
            } else {
                Logger.SUC(this.getClass().getName(), "BACKED UP file " + fileId + ", with repDeg " + repDeg);
                BackupTasks.removeBackupTask(fileId);
                // TODO: update state here.
                BackupTasks.removeTrackFile(fileId);
            }

        }
    }
}
