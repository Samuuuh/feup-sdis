package network.services.delete;

import network.Main;
import network.etc.FileHandler;
import network.etc.Logger;
import network.message.MessageDelete;
import network.node.InfoNode;

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
                System.out.println("Origin Node");
            } else if(false) {
                System.out.println("Already proces");
            } else {
                if (Main.state.getStoredFile(fileName) != null) {
                    Main.state.deleteStored(fileName);
                    
                    FileHandler.deleteFile(port + "/backup/", fileName.substring(0, fileName.lastIndexOf('.')) + ".ser");
                }
                InfoNode sucessor = Main.chordNode.getSuccessor();
                Main.threadPool.execute(new SendDelete(sucessor.getIp(), sucessor.getPort(), fileName, message.getOriginNode()));
            } 
        } catch(Exception e) {
            Logger.ERR(this.getClass().getName(), "Not possible to send restore message");
        }
    }
}
