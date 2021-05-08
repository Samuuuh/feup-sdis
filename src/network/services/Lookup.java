package network.services;

import network.ChordNode;
import network.Main;
import network.message.MessageLookup;

import java.math.BigInteger;

public class Lookup {

    MessageLookup message;
    ChordNode chordNode;
    public Lookup(MessageLookup message){
        this.message = message;
        this.chordNode = Main.chordNode;
    }

    public void findSuccessor(){
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
