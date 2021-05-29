package network.services.delete;

import java.util.concurrent.TimeUnit;

import network.Main;
import network.etc.FileHandler;
import network.etc.Logger;
import network.message.MessageDelete;

public class ProcessDelete implements Runnable {
    MessageDelete message;
    String fileName;
    int port;

    public ProcessDelete(MessageDelete message, int port) {
        this.message = message;
        this.fileName = message.getFileName();
        this.port = port;
    }

    @Override
    public void run() {
        try {
            if (message.getPortOrigin() == port) {
                Logger.ANY(this.getClass().getName(), "Origin Node");
            } else if(Main.state.getBlockDeleteMessages(fileName) != null) {
                Logger.ANY(this.getClass().getName(), "Already processed DELETE.");
            } else {
                if (Main.state.getStoredFile(fileName) != null) {
                    Main.state.deleteStored(fileName);
                    
                    FileHandler.deleteFile(port + "/backup/", fileName.substring(0, fileName.lastIndexOf('.')) + ".ser");
                }

                Main.state.addBlockDeleteMessages(fileName);
                Main.schedulerPool.schedule(new RemoveBlockDelete(fileName), 3 * 1000L, TimeUnit.MILLISECONDS);
                Main.threadPool.execute(new SendDelete(fileName, message.getOriginNode()));
            } 
        } catch(Exception e) {
            Logger.ERR(this.getClass().getName(), "Not possible to send restore message");
        }
    }
}
