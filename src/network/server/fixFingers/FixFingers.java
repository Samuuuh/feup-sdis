package network.server.fixFingers;

import network.Main;
import network.etc.Logger;
import network.etc.MessageType;
import network.etc.Singleton;
import network.message.MessageLookup;
import network.node.InfoNode;
import network.server.com.SendMessage;

import java.math.BigInteger;
import java.util.concurrent.Callable;

public class FixFingers implements Callable {

    @Override
    public Boolean call() {
        try {
            InfoNode successor = Main.chordNode.getSuccessor();
            if ((successor == null) || (successor == Main.chordNode.getInfoNode()))
                return true;

            Integer next = Main.chordNode.getNext();
            next += 1;

            Main.chordNode.setNext(next);
            if (next > Singleton.m) {
                Main.chordNode.setNext(1);
                next = 1;
            }

            BigInteger currentId = Main.chordNode.getId();
            BigInteger nextId = new BigInteger(String.valueOf((long) Math.pow(2, next - 1)));
            BigInteger targetId = currentId.add(nextId);
            MessageLookup messageLookup = new MessageLookup(Main.chordNode.getInfoNode(), targetId, MessageType.FIX_FINGERS);

            new SendMessage(successor.getIp(), successor.getPort(), messageLookup).call();

        }catch(Exception e){
            Logger.ERR(this.getClass().getName(), "Error on fix fingers.");
            // Case node leaves the network.
            Main.chordNode.fixSuccessor();
        }

        return true;
    }


}


