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
        try {
            if (Main.state.getBlockReclaimMessages(messageReclaim.getMessageId()) != null) {
                return;
            } else if (Main.chordNode.getId().equals(messageReclaim.getTargetId())) {
                Logger.INFO(this.getClass().getName(), "Received reclaim");
                Main.state.addBlockReclaimMessages(messageReclaim.getMessageId());
                Main.schedulerPool.schedule(new UnbanReclaim(messageReclaim.getMessageId()), 3 * 1000L, TimeUnit.MILLISECONDS);
                reclaim();
            } else {
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
        for (var hash: toDelete){
            Main.threadPool.submit(new ResendBackupFile(successorIp, successorPort, hash, infoNode, 1));
            Main.schedulerPool.schedule(new DeleteBackupFile(hash), 2000, TimeUnit.MILLISECONDS);
        }

        Logger.ANY(this.getClass().getName(), "Reclaim done with success.");
    }

    /**
     * The chosen files need to be backed up to another peer.
     */
    private void chooseFilesToDelete(){
        Main.state.getStoredFiles().forEach((hash, size)->{
            int occupiedSize = Main.state.getOccupiedSize();
            if (occupiedSize <= Main.state.getMaxSize())
                return;

            Integer fileSize = Main.state.removeFile(hash);
            if (fileSize != null)
                toDelete.add(hash);
        });
    }

    static class DeleteBackupFile implements Runnable{
        String hash;
        public DeleteBackupFile(String hash){
            this.hash= hash;
        }

        @Override
        public void run() {

            FileHandler.deleteFile("peers/" + Main.chordNode.getId() + "/backup/" ,hash + ".ser");
        }
    }
}
