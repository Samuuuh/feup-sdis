package network.services;

import network.ChordNode;
import network.Main;
import network.message.MessageLookup;
import network.message.MessageSuccessor;
import network.message.OK;
import network.node.InfoNode;
import network.server.com.SendMessage;

import java.math.BigInteger;

public class Lookup implements Runnable{

    MessageLookup message;
    ChordNode chordNode;

    public Lookup(MessageLookup message){
        this.message = message;
        this.chordNode = Main.chordNode;
    }

    @Override
    public void run() {
        BigInteger targetId= message.getTargetId();
        BigInteger currentId = Main.chordNode.getInfoNode().getId();
        BigInteger successorId = Main.chordNode.getSuccessor().getId();

        if (between(targetId, currentId, successorId)) {
            MessageSuccessor messageSuccessor = new MessageSuccessor(Main.chordNode.getInfoNode(), Main.chordNode.getInfoNode());
            Main.threadPool.execute(new SendMessage(message.getIpOrigin(), message.getPortOrigin(), messageSuccessor));
            System.out.println("Vou entrar no loop infinito?");
        }
        else closestPrecedingNode(targetId);

    }

    public void closestPrecedingNode(BigInteger targetId){
        var fingerTableOrder = Main.chordNode.getFingerTableOrder();
        var fingerTable = Main.chordNode.getFingerTable();

        BigInteger currentId = Main.chordNode.getInfoNode().getId();

        // Ask for the successor to the next node.
        for (BigInteger id: fingerTableOrder){
            if (between(targetId, currentId, id)) {
                InfoNode closestNode = fingerTable.get(id);
                MessageLookup messageLookup = new MessageLookup(message.getOriginNode(), message.getTargetId());
                Main.threadPool.execute(new SendMessage(closestNode.getIp(), closestNode.getPort(), messageLookup));
            }
        }
        // The own node is the successor.
        MessageSuccessor messageSuccessor = new MessageSuccessor(Main.chordNode.getInfoNode(), Main.chordNode.getInfoNode());
        Main.threadPool.execute(new SendMessage(message.getIpOrigin(), message.getPortOrigin(), messageSuccessor));
    }



    /**
     * if nodeId > successorId, then it will a turn in the circle.
     * We need to consider the case above to calculate if an id is between others.
     * @param targetId Id that we want to discover the successor.
     * @param nodeId Id of the current node.
     * @param successorId Successor of the current node.
     * @return return a boolean, true case is between the range, else otherwise.
     */
    public boolean between(BigInteger targetId, BigInteger nodeId, BigInteger successorId){
        if (nodeId.compareTo(successorId) < 0){
            return targetId.compareTo(nodeId) > 0 && targetId.compareTo(successorId) < 0;
        } else{
            return targetId.compareTo(nodeId) > 0 || targetId.compareTo(successorId) < 0;
        }
    }


}
