package network.services.reclaim;

import network.Main;
import network.etc.MessageType;
import network.etc.Singleton;
import network.message.reclaim.MessageReclaim;

import java.math.BigInteger;
import java.util.concurrent.Callable;

/**
 * This file is responsible for finding the peer of which the reclaim was requested.
 */
public class RequestReclaim implements Callable {
    int port;
    int reclaimSize;
    BigInteger targetId;    // This is the identification of the peer to do the reclaim.

    public RequestReclaim(String ip, int port, int reclaimSize){
        this.reclaimSize = reclaimSize;
        this.port = port;
        this.targetId = Singleton.encode(Singleton.getIdUncoded(ip, port));
    }

    @Override
    public Object call() throws Exception {
        MessageReclaim messageReclaim = new MessageReclaim(Main.chordNode.getInfoNode(), MessageType.RECLAIM, this.targetId, this.reclaimSize);

        var fingerTableOrder = Main.chordNode.getFingerTableOrder();
        var fingerTable = Main.chordNode.getFingerTable();

        var iterator = fingerTableOrder.descendingIterator();
        while(iterator.hasNext()){
            System.out.println(iterator.next());
        }
        return null;
    }






}
