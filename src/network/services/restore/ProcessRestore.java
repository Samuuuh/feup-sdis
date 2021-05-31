package network.services.restore;

import network.Main;
import network.etc.FileHandler;
import network.etc.Logger;
import network.etc.Singleton;
import network.message.MessageRcvRestore;

public class ProcessRestore implements Runnable {
    MessageRcvRestore message;

    /**
     * Process the restore of the message
     * @param messageRestore restore message
     */
    public ProcessRestore(MessageRcvRestore messageRestore) {
        this.message = messageRestore;
    }

    /**
     * Run the process restore
     */
    @Override
    public void run() {
        Logger.ANY(this.getClass().getName(), "Start RESTORE");
        String filePath = message.getFileName();

        saveFile(filePath, message);
    }

    /**
     * Save the file in filesystem
     * @param filePath path of the file to save
     * @param message restore message
     */
    public void saveFile(String filePath, MessageRcvRestore message) {
        try {
            String fileName = Singleton.getFileName(filePath);
            String fileExtension = Singleton.getFileExtension(filePath);
            FileHandler.saveFile("peers/" + Main.chordNode.getId() + "/restore/", fileName + "." + fileExtension, message.getBytes());
        }catch(Exception e) {
            Logger.ERR(this.getClass().getName(), "Not possible to save file " + filePath);
        }
    }
}
