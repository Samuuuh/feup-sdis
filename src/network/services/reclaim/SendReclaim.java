package network.services.reclaim;

import network.Main;
import network.etc.MessageType;
import network.message.reclaim.MessageReclaim;
import network.node.InfoNode;
import network.server.com.SendMessage;

import java.math.BigInteger;
import java.util.concurrent.Callable;

/**
 * This file is responsible for finding the peer of which the reclaim was requested.
 */
public class SendReclaim implements Callable {
    int reclaimSize;
    MessageReclaim messageReclaim;

    /**
     * Constructor to send reclaim message
     * @param targetId id of peer to be reclaimed
     * @param reclaimSize size to be reclaimed
     */
    public SendReclaim(BigInteger targetId, int reclaimSize){
        this.reclaimSize = reclaimSize;
        messageReclaim = new MessageReclaim(Main.chordNode.getInfoNode(), MessageType.RECLAIM, targetId, this.reclaimSize);
    }

    /**
     * Constructor to send reclaim message
     * @param messageReclaim message received
     */
    public SendReclaim(MessageReclaim messageReclaim){
        this.messageReclaim = messageReclaim;
    }

    /**
     * Send reclaim message
     */
    @Override
    public Object call() throws Exception {

        var fingerTableOrder = Main.chordNode.getFingerTableOrder();
        var fingerTable = Main.chordNode.getFingerTable();

        var iterator = fingerTableOrder.descendingIterator();
        Main.chordNode.printHashTable();
        while(iterator.hasNext()){
            BigInteger next = iterator.next();
            InfoNode infoNext = fingerTable.get(next);
            Main.threadPool.submit(new SendMessage(infoNext.getIp(), infoNext.getPort(), messageReclaim));
        }
        return null;
    }
}
