package network.message;

import network.node.InfoNode;

import java.math.BigInteger;

public class MessageLookup extends Message{


    BigInteger targetId;
    /**
     * Finds out the successor of a node.
     * @param ip Ip of the sender.
     * @param port Port of the sender.
     * @param targetId Id of which we wanna find out the successor.
     */
    public MessageLookup(InfoNode originNode, BigInteger targetId){
        super(originNode, "lookup");
        this.targetId = targetId;
    }

    public BigInteger getTargetId(){
        return targetId;
    }
}