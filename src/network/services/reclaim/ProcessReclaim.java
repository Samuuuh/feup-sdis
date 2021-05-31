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
public class ProcessReclaim implements Runnable{
    MessageReclaim messageReclaim;
    ArrayList<String> toDelete;          // Contains the files chosen to be deleted.

    /**
     * Process the reclaim
     * @param messageReclaim message received
     */
    public ProcessReclaim(MessageReclaim messageReclaim){
        this.messageReclaim = messageReclaim;
        toDelete = new ArrayList<>();
    }

    /**
     * Run the reclaim
     */
    @Override
    public void run() {
        // Message still banned.
        System.out.println(messageReclaim.getTargetId());
        Logger.INFO(this.getClass().getName(), "Received reclaim");
        try {
            if (Main.state.getBlockReclaimMessages(messageReclaim.getMessageId()) != null) {
                System.out.println("here1");
                return;
            } else if (Main.chordNode.getId().equals(messageReclaim.getTargetId())) {
                System.out.println("here2");
                Main.state.addBlockReclaimMessages(messageReclaim.getMessageId());
                Main.schedulerPool.schedule(new UnbanReclaim(messageReclaim.getMessageId()), 3 * 1000L, TimeUnit.MILLISECONDS);
                reclaim();
            } else {
                System.out.println("here3");
                Main.state.addBlockReclaimMessages(messageReclaim.getMessageId());
                Main.schedulerPool.schedule(new UnbanReclaim(messageReclaim.getMessageId()), 3 * 1000L, TimeUnit.MILLISECONDS);
                Main.threadPool.submit(new SendReclaim(this.messageReclaim));
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Reclaim request
     */
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

    // TODO: vai ser eliminado???
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
