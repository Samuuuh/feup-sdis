package network.services;

import network.ChordNode;
import network.Main;
import network.etc.MessageType;
import network.etc.Singleton;
import network.message.MessageLookup;
import network.message.MessageSuccessor;
import network.node.InfoNode;
import network.server.com.SendMessage;

import java.io.IOException;
import java.math.BigInteger;

public class Lookup implements Runnable {

    MessageLookup message;
    ChordNode chordNode;
    MessageType returnType;
    MessageType forwardType;

    public Lookup(MessageLookup message, MessageType returnType, MessageType forwardType) {
        this.message = message;
        this.chordNode = Main.chordNode;
        this.returnType = returnType;
        this.forwardType = forwardType;
    }

    @Override
    public void run() {
        try {
            BigInteger targetId = message.getTargetId();
            BigInteger currentId = Main.chordNode.getInfoNode().getId();
            BigInteger successorId = Main.chordNode.getSuccessor().getId();

            if (Singleton.betweenSuccessor(targetId, currentId, successorId)) {
                MessageSuccessor messageSuccessor = new MessageSuccessor(Main.chordNode.getInfoNode(), targetId, Main.chordNode.getSuccessor(), this.returnType);

                //System.out.println("targetId " + targetId + " is between currentId "+ currentId + " and successor ID " + successorId);
                new SendMessage(message.getIpOrigin(), message.getPortOrigin(), messageSuccessor).call();
            } else closestPrecedingNode(targetId);

        } catch (Exception e) {
        }
    }

    public void closestPrecedingNode(BigInteger targetId) throws IOException, ClassNotFoundException {
        var fingerTableOrder = Main.chordNode.getFingerTableOrder();
        var fingerTable = Main.chordNode.getFingerTable();

        BigInteger currentId = Main.chordNode.getInfoNode().getId();

        var iterator = fingerTableOrder.descendingIterator();
        while (iterator.hasNext()) {
            BigInteger id = iterator.next();
            if (Singleton.betweenSuccessor(targetId, currentId, id)) {
                InfoNode closestNode = fingerTable.get(id);
                if (closestNode == null) break;
                MessageLookup messageLookup = new MessageLookup(message.getOriginNode(), targetId, this.forwardType);
                Main.threadPool.submit(new SendMessage(closestNode.getIp(), closestNode.getPort(), messageLookup));
                return;
            }
        }

        // The own node is the successor.
        System.out.println(message.getPortOrigin());
        //System.out.println("successor of " + targetId + " is " + Main.chordNode.getInfoNode().getId());
        MessageSuccessor messageSuccessor = new MessageSuccessor(Main.chordNode.getInfoNode(), targetId, Main.chordNode.getInfoNode(), this.returnType);
        Main.threadPool.submit(new SendMessage(message.getIpOrigin(), message.getPortOrigin(), messageSuccessor));
    }
}
