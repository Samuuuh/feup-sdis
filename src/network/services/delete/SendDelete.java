package network.services.delete;

import java.math.BigInteger;
import java.util.concurrent.TimeUnit;

import network.Main;
import network.etc.FileHandler;
import network.etc.Logger;
import network.etc.Singleton;
import network.server.com.*;
import network.message.*;
import network.node.InfoNode;

public class SendDelete implements Runnable {
    String filePath;
    InfoNode originNode;

    public SendDelete(String filePath, InfoNode originNode) {
         this.filePath = filePath;
         this.originNode = originNode;
    }

    @Override
    public void run() {

        try {
            Main.state.addBlockDeleteMessages(filePath);
            if (Main.state.getStoredFile(filePath) != null) {
                Main.state.removeFile(filePath);
                FileHandler.deleteFile("peers/" + Main.chordNode.getId() + "/backup/", Singleton.getFileName(filePath) + ".ser");
            }

            Main.schedulerPool.schedule(new RemoveBlockDelete(filePath), 3 * 1000L, TimeUnit.MILLISECONDS);
            MessageDelete message = new MessageDelete(originNode, filePath);

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
