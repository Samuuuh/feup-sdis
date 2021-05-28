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

/**
 * Implements the Lookup protocol from the Chord paper.
 * https://pdos.csail.mit.edu/papers/ton:chord/paper-ton.pdf
 */
public class Lookup implements Runnable {

    MessageLookup message;      // Message that requested the lookup.
    ChordNode chordNode;
    MessageType returnType;     // Type of message to be returned.
    MessageType forwardType;    // Type of message to be forward if necessary.

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
            BigInteger peerId = Main.chordNode.getInfoNode().getId();
            BigInteger successorId = Main.chordNode.getSuccessor().getId();

            if (Singleton.betweenSuccessor(targetId, peerId, successorId)) {
                MessageSuccessor messageSuccessor = new MessageSuccessor(Main.chordNode.getInfoNode(), targetId, Main.chordNode.getSuccessor(), this.returnType);
                new SendMessage(message.getIpOrigin(), message.getPortOrigin(), messageSuccessor).call();
            } else closestPrecedingNode(targetId);

        } catch (Exception e) {
            Logger.ERR(this.getClass().getName(), "Error on processing lookup...");
        }
    }

    /**
     * Get the closest node from the target.
     * @param targetId Peer of which wants to find out the successor.
     */
    public void closestPrecedingNode(BigInteger targetId) throws IOException {
        var fingerTableOrder = Main.chordNode.getFingerTableOrder();
        var fingerTable = Main.chordNode.getFingerTable();

        BigInteger peerId = Main.chordNode.getInfoNode().getId();
        var iterator = fingerTableOrder.descendingIterator();

        while (iterator.hasNext()) {
            BigInteger id = iterator.next();
            if (Singleton.betweenSuccessor(targetId, peerId, id)) {
                InfoNode closestNode = fingerTable.get(id);
                if (closestNode == null) continue;

                MessageLookup messageLookup = new MessageLookup(message.getOriginNode(), targetId, this.forwardType);
                Main.threadPool.submit(new SendMessage(closestNode.getIp(), closestNode.getPort(), messageLookup));
                return;
            }
        }

        // The own node is the successor.
        MessageSuccessor messageSuccessor = new MessageSuccessor(Main.chordNode.getInfoNode(), targetId, Main.chordNode.getInfoNode(), this.returnType);
        Main.threadPool.submit(new SendMessage(message.getIpOrigin(), message.getPortOrigin(), messageSuccessor));
    }
}
