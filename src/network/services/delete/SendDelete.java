package network.services.delete;

import java.math.BigInteger;

import network.Main;
import network.etc.Logger;
import network.server.com.*;
import network.message.*;
import network.node.InfoNode;

public class SendDelete implements Runnable {
    String filePath;
    int repDeg;
    InfoNode originNode;

    public SendDelete(String filePath, InfoNode originNode) {
         this.filePath = filePath;
         this.originNode = originNode;
    }

    @Override
    public void run() {
        try {
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
