package network.services;

import network.ChordNode;
import network.Main;
import network.etc.MessageType;
import network.etc.Singleton;
import network.message.Message;
import network.message.MessageLookup;
import network.message.MessageInfoNode;
import network.message.MessageSuccessor;
import network.node.InfoNode;
import network.server.com.SendMessage;

import java.math.BigInteger;
import java.util.Iterator;

public class Lookup implements Runnable{

    MessageLookup message;
    ChordNode chordNode;
    MessageType returnType;
    MessageType forwardType;

    public Lookup(MessageLookup message, MessageType returnType, MessageType forwardType){
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
            MessageSuccessor messageSuccessor = new MessageSuccessor(Main.chordNode.getInfoNode(), targetId, Main.chordNode.getInfoNode(), this.returnType);
            Main.threadPool.execute(new SendMessage(message.getIpOrigin(), message.getPortOrigin(), messageSuccessor));
            System.out.println("Sending message with targetId: " + targetId + " to " + message.getIpOrigin());
        }
        else closestPrecedingNode(targetId);
    }

    public void closestPrecedingNode(BigInteger targetId){
        var fingerTableOrder = Main.chordNode.getFingerTableOrder();
        var fingerTable = Main.chordNode.getFingerTable();

        BigInteger currentId = Main.chordNode.getInfoNode().getId();

        var iterator = fingerTableOrder.descendingIterator();
        while(iterator.hasNext()){
            BigInteger id = iterator.next();
            if (Singleton.betweenSuccessor(targetId, currentId, id)) {

                InfoNode closestNode = fingerTable.get(id);
                MessageLookup messageLookup = new MessageLookup(message.getOriginNode(), targetId, this.forwardType);
                System.out.println("The closest node is " + id.toString());
                Main.threadPool.execute(new SendMessage(closestNode.getIp(), closestNode.getPort(), messageLookup));
                break;
            }
        }

        // The own node is the successor.
        System.out.println("Sending message with targetId: " + targetId + " to " + message.getIpOrigin());
        MessageSuccessor messageSuccessor = new MessageSuccessor(Main.chordNode.getInfoNode(), targetId, Main.chordNode.getInfoNode(), this.returnType);
        Main.threadPool.execute(new SendMessage(message.getIpOrigin(), message.getPortOrigin(), messageSuccessor));
    }


}
