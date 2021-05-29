package network.services.reclaim;

import network.Main;
import network.etc.FileHandler;
import network.etc.Logger;
import network.etc.Singleton;
import network.message.reclaim.MessageReclaim;
import network.node.InfoNode;
import network.services.backup.ResendBackupFile;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

/**
 * After receiving the reclaim message this class will
 * decide what to do with it.
 */
public class Reclaim implements Runnable{


    MessageReclaim messageReclaim;
    ArrayList<String> toDelete;          // Contains the files chosen to be deleted.

    public Reclaim(MessageReclaim messageReclaim){
        this.messageReclaim = messageReclaim;
        toDelete = new ArrayList<>();
    }
    @Override
    public void run() {
        // Message still banned.
        if (Main.bannedReclaimMessages.contains(messageReclaim.getMessageId())) {
            return;
        }
        else if (Main.chordNode.getId().equals(messageReclaim.getTargetId())){
            Main.bannedReclaimMessages.add(messageReclaim.getMessageId());
            Main.schedulerPool.schedule(new UnbanReclaim(messageReclaim.getMessageId()), 3 * 1000L, TimeUnit.MILLISECONDS);
            reclaim();
        }else{
            Main.bannedReclaimMessages.add(messageReclaim.getMessageId());
            Main.schedulerPool.schedule(new UnbanReclaim(messageReclaim.getMessageId()), 3 * 1000L, TimeUnit.MILLISECONDS);
            Main.threadPool.submit(new RequestReclaim(this.messageReclaim));
        }
    }

    private void reclaim(){
        Main.state.setMaxSize(messageReclaim.getSize());
        chooseFilesToDelete();

        String successorIp = Main.chordNode.getSuccessor().getIp();
        int successorPort = Main.chordNode.getSuccessor().getPort();
        InfoNode infoNode = Main.chordNode.getInfoNode();
        for (var filePath: toDelete){
            Main.threadPool.submit(new ResendBackupFile(successorIp, successorPort, filePath, infoNode, 1));
            Main.schedulerPool.schedule(new DeleteBackupFile(filePath), 500, TimeUnit.MILLISECONDS);
        }

        Logger.ANY(this.getClass().getName(), "Reclaim done with success.");
    }

    /**
     * The chosen files need to be backed up to another peer.
     */
    private void chooseFilesToDelete(){
        Main.state.getStoredFiles().forEach((filePath, size)->{
            int occupiedSize = Main.state.getOccupiedSize();
            if (occupiedSize <= Main.state.getMaxSize())
                return;

            Integer fileSize = Main.state.removeFile(filePath);
            if (fileSize != null) {
                String fileName = Singleton.getFileName(filePath);
                String filePathBackup = Singleton.getBackupFilePath(fileName);
                toDelete.add(filePathBackup);
            }
        });
    }

    static class DeleteBackupFile implements Runnable{
        String filePath;
        public DeleteBackupFile(String filePath){
            this.filePath = filePath;
        }

        @Override
        public void run() {
            String fileName = Singleton.getFileName(filePath);
            String filePathBackup = Singleton.getBackupFilePath(fileName);
            System.out.println(filePathBackup);
            FileHandler.DeleteFile(filePathBackup);
        }
    }
}
