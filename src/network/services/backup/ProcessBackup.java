package network.services.backup;

import network.Main;
import network.etc.FileHandler;
import network.etc.Logger;
import network.etc.MessageType;
import network.etc.Singleton;
import network.message.MessageBackup;
import network.message.MessageStored;
import network.message.MessageDoneBackup;
import network.node.InfoNode;
import network.server.com.SendMessage;

import java.io.IOException;

public class ProcessBackup implements Runnable {
    MessageBackup message;

    /**
     * Process the backup message
     * @param messageBackup backup message received
     */
    public ProcessBackup(MessageBackup messageBackup) {
        this.message = messageBackup;
    }

    /**
     * Run the process backup
     */
    @Override
    public void run() {
        try {
            Logger.ANY(this.getClass().getName(), "Received BACKUP");
            int desiredRepDeg = message.getDesiredRepDeg();
            String filePath = message.getFileName();

            if (Main.state.getOccupiedSize() + message.getBytes().length > Main.state.getMaxSize()){
                Logger.INFO(this.getClass().getName(), "Not enough space to save " + Singleton.getFileName(filePath) + "... Parsing to successor.");
                sendToSuccessor(message.getActualRepDeg());
            }
            if (Main.state.getStoredFile(filePath) != null) {
                sendToSuccessor(message.getActualRepDeg());
                return;
            }

            Main.state.addStoredFile(message.getFileName(), message.getBytes().length);

            saveFile(filePath, message);
            int actualRepDeg = message.getActualRepDeg() + 1;

            if (actualRepDeg == desiredRepDeg) {
                sendBackupDone(filePath);
            } else {
                sendToSuccessor(message.getActualRepDeg() + 1);
                storedMessageOrigin(filePath);
            }
        }catch(Exception e){
            Logger.ERR(this.getClass().getName(), "Not possible to send backup message");
        }
    }

    /**
     * Save the file in filesystem
     * @param filePath the path of the file
     * @param message the message received
     */
    public void saveFile(String filePath, MessageBackup message) {
        try {
            String fileName = Singleton.getFileName(filePath);
            FileHandler.saveSerialize("peers/" + Main.chordNode.getId() + "/backup/",fileName + ".ser", message);
        }catch(Exception e) {
            Logger.ERR(this.getClass().getName(), "Not possible to save file " + filePath);
        }
    }

    /**
    * Send message to sucessor
    * @param actualRepDeg actual replication degree of the file
     */
    public void sendToSuccessor(int actualRepDeg) throws IOException {
        int desiredRepDeg = message.getDesiredRepDeg();
        byte[] bytesMessage = message.getBytes();
        String filePath = message.getFileName();

        InfoNode suc = Main.chordNode.getSuccessor();
        MessageBackup newMessage = new MessageBackup(message.getOriginNode(), filePath, bytesMessage, desiredRepDeg, actualRepDeg);
        Main.threadPool.submit(new SendMessage(suc.getIp(), suc.getPort(), newMessage));
    }

    /**
     * Send a message to say that the file was backup
     * @param filePath file that was backup
     */
    public void storedMessageOrigin(String filePath) throws IOException {
        InfoNode currentNode = Main.chordNode.getInfoNode();
        MessageStored messageStored = new MessageStored(currentNode, MessageType.STORED, filePath);
        Main.threadPool.submit(new SendMessage(message.getIpOrigin(), message.getPortOrigin(), messageStored));
    }

    /**
     * Indicates that backup is already done
     * @param filePath file that was backup
     */
    public void sendBackupDone(String filePath) throws IOException {
        int desiredRepDeg = message.getDesiredRepDeg();
        int actualRepDeg = message.getActualRepDeg() + 1;

        MessageDoneBackup messageDone = new MessageDoneBackup(message.getOriginNode(), MessageType.DONE_BACKUP, desiredRepDeg, actualRepDeg, filePath);
        Main.threadPool.submit(new SendMessage(message.getIpOrigin(), message.getPortOrigin(), messageDone));
    }
}
