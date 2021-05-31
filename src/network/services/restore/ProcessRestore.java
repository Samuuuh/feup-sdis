package network.services.restore;

import network.Main;
import network.etc.FileHandler;
import network.etc.Logger;
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
        String fileName = message.getFileName();

        saveFile(fileName, message);
    }

    /**
     * Save the file in filesystem
     * @param fileName path of the file to save
     * @param message restore message
     */
    public void saveFile(String fileName, MessageRcvRestore message) {
        try {
            FileHandler.saveFile("peers/" + Main.chordNode.getId() + "/restore/", fileName, message.getBytes());
        }catch(Exception e) {
            Logger.ERR(this.getClass().getName(), "Not possible to save file " + fileName);
        }
    }
}
