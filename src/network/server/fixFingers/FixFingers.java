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
    private final int currentNext;

    public FixFingers(int currentNext) {
        this.currentNext = currentNext;
    }

    @Override
    public Boolean call() {

        try {
            InfoNode successor = Main.chordNode.getSuccessor();

            if ((successor == null) || (successor == Main.chordNode.getInfoNode()))
                return true;

            BigInteger currentId = Main.chordNode.getId();
            BigInteger nextId = new BigInteger(String.valueOf((long) Math.pow(2, currentNext - 1)));
            BigInteger targetId = currentId.add(nextId);
            MessageLookup messageLookup = new MessageLookup(Main.chordNode.getInfoNode(), targetId, MessageType.FIX_FINGERS);
            System.out.println("Find the successor of " + targetId);
            new SendMessage(successor.getIp(), successor.getPort(), messageLookup).call();
        } catch (Exception e) {
            e.printStackTrace();
            Logger.ERR(this.getClass().getName(), "Error on fix fingers.");
            Main.chordNode.fixSuccessor();
        }
        return true;
    }


}


