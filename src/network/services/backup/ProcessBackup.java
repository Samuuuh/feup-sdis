package network.services.backup;

import network.Main;
import network.etc.FileHandler;
import network.etc.Logger;
import network.etc.MessageType;
import network.message.MessageBackup;
import network.message.MessageDoneBackup;
import network.message.MessageStored;
import network.node.InfoNode;
import network.server.com.SendMessage;

import java.io.IOException;

public class ProcessBackup implements Runnable {

    MessageBackup message;

    public ProcessBackup(MessageBackup messageBackup) {
        this.message = messageBackup;
    }

    @Override
    public void run() {
        Logger.ANY(this.getClass().getName(), "Received BACKUP");
        int desiredRepDeg = message.getDesiredRepDeg();
        byte[] bytesMessage = message.getBytes();
        String filePath = message.getFileName();

        saveFile(filePath, bytesMessage);
        int actualRepDeg = message.getActualRepDeg() + 1;

        if (actualRepDeg == desiredRepDeg) {
            sendBackupDone();
        } else {
            sendToSuccessor();
            storedMessageOrigin();
        }
    }

    // TODO: Verificar se o peer pode salvar/salvou o ficheiro
    public void saveFile(String filePath, byte[] bytesMessage) {
        try {
            int port = Main.chordNode.getInfoNode().getPort();
            FileHandler.saveFile(port + "/backup/", filePath, bytesMessage);
        }catch(Exception e){
            Logger.ERR(this.getClass().getName(), "Not possible to save file " + filePath);
        }
    }

    public void sendToSuccessor(){
        int desiredRepDeg = message.getDesiredRepDeg();
        byte[] bytesMessage = message.getBytes();
        String filePath = message.getFileName();
        int actualRepDeg = message.getActualRepDeg() + 1;

        InfoNode suc = Main.chordNode.getSuccessor();
        MessageBackup newMessage = new MessageBackup(message.getOriginNode(), filePath, bytesMessage, desiredRepDeg, actualRepDeg);
        Main.threadPool.execute(new SendMessage(suc.getIp(), suc.getPort(), newMessage));
    }

    public void storedMessageOrigin(){
        InfoNode currentNode = Main.chordNode.getInfoNode();
        MessageStored messageStored = new MessageStored(currentNode, MessageType.STORED);
        Main.threadPool.execute(new SendMessage(message.getIpOrigin(), messageStored.getPortOrigin(), messageStored));
    }

    public void sendBackupDone(){
        int desiredRepDeg = message.getDesiredRepDeg();
        int actualRepDeg = message.getActualRepDeg() + 1;

        MessageDoneBackup messageDone = new MessageDoneBackup(message.getOriginNode(), MessageType.DONE_BACKUP, desiredRepDeg, actualRepDeg);
        Main.threadPool.execute(new SendMessage(message.getIpOrigin(), message.getPortOrigin(), messageDone));
    }
}
