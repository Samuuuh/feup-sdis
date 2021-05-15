package network.services;

import network.ChordNode;
import network.Main;
import network.etc.MessageType;
import network.etc.Singleton;
import network.message.Message;
import network.message.MessageLookup;
import network.message.MessageInfoNode;
import network.node.InfoNode;
import network.server.com.SendMessage;

import java.math.BigInteger;

public class Lookup implements Runnable{

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
        BigInteger targetId = message.getTargetId();
        BigInteger currentId = Main.chordNode.getInfoNode().getId();
        BigInteger successorId = Main.chordNode.getSuccessor().getId();

        if (Singleton.betweenSuccessor(targetId, currentId, successorId)) {
            MessageInfoNode messageInfoNode = new MessageInfoNode(Main.chordNode.getInfoNode(), this.returnType, Main.chordNode.getInfoNode());
            Main.threadPool.execute(new SendMessage(message.getIpOrigin(), message.getPortOrigin(), messageInfoNode));
        }
        else closestPrecedingNode(targetId);

    }

    public void closestPrecedingNode(BigInteger targetId){
        var fingerTableOrder = Main.chordNode.getFingerTableOrder();
        var fingerTable = Main.chordNode.getFingerTable();

        BigInteger currentId = Main.chordNode.getInfoNode().getId();

        //TODO: redo, de tras pra frente.
        // Ask for the successor to the next node.
        for (BigInteger id: fingerTableOrder){
            if (Singleton.betweenSuccessor(targetId, currentId, id)) {
                InfoNode closestNode = fingerTable.get(id);
                MessageLookup messageLookup = new MessageLookup(message.getOriginNode(), message.getTargetId(), this.forwardType);
                Main.threadPool.execute(new SendMessage(closestNode.getIp(), closestNode.getPort(), messageLookup));
            }
        }
        // The own node is the successor.
        MessageInfoNode messageInfoNode = new MessageInfoNode(Main.chordNode.getInfoNode(), this.returnType, Main.chordNode.getInfoNode());
        Main.threadPool.execute(new SendMessage(message.getIpOrigin(), message.getPortOrigin(), messageInfoNode));
    }


}
