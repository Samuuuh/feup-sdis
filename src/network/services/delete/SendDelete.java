package network.services.delete;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import network.Main;
import network.etc.FileHandler;
import network.etc.Logger;
import network.server.com.*;
import network.message.*;
import network.node.InfoNode;

public class SendDelete implements Runnable {
    String fileName;
    String hash;
    InfoNode originNode;

    /**
     * Send Delete Protocol constructor
     * @param fileName Path of file to delete
     * @param originNode origin node of delete
     */
    public SendDelete(String hash, InfoNode originNode) {
         this.hash = hash;
         this.originNode = originNode;
    }

    /**
    * Run the send selete protocol message
    */
    @Override
    public void run() {
        try {
            Main.state.addBlockDeleteMessages(hash);
            if (Main.state.getStoredFile(hash) != null) {
                Main.state.removeFile(hash);
                FileHandler.deleteFile("peers/" + Main.chordNode.getId() + "/backup/", hash + ".ser");
            }

            Main.schedulerPool.schedule(new RemoveBlockDelete(hash), 3 * 1000L, TimeUnit.MILLISECONDS);
            MessageDelete message = new MessageDelete(originNode, hash);

            var fingerTableOrder = Main.chordNode.getFingerTableOrder();
            var fingerTable = Main.chordNode.getFingerTable();

            var iterator = fingerTableOrder.descendingIterator();
            while(iterator.hasNext()){
                BigInteger next = iterator.next();
                InfoNode infoNext = fingerTable.get(next);
                Main.threadPool.submit(new SendMessage(infoNext.getIp(), infoNext.getPort(), message));
            }
            
        } catch(Exception e){
            Logger.ERR(this.getClass().getName(), "Not possible to send restore message");
        }
    }
}
