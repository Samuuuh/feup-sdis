package network.services;

import network.ChordNode;
import network.Main;
import network.etc.Logger;
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
                MessageSuccessor messageSuccessor = new MessageSuccessor(Main.chordNode.getInfoNode(), targetId, Main.chordNode.getInfoNode(), this.returnType);
                new SendMessage(message.getIpOrigin(), message.getPortOrigin(), messageSuccessor).call();
            } else closestPrecedingNode(targetId);

        } catch (Exception e) {
            Logger.ERR(this.getClass().getName(), "Error calculating closest preceding Node or sending message.");
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
                MessageLookup messageLookup = new MessageLookup(message.getOriginNode(), targetId, this.forwardType);
                new SendMessage(closestNode.getIp(), closestNode.getPort(), messageLookup).call();
                break;
            }
        }

        // The own node is the successor.
        MessageSuccessor messageSuccessor = new MessageSuccessor(Main.chordNode.getInfoNode(), targetId, Main.chordNode.getInfoNode(), this.returnType);
        new SendMessage(message.getIpOrigin(), message.getPortOrigin(), messageSuccessor).call();
    }


}
