package network.message;

import network.etc.MessageType;
import network.node.InfoNode;

import java.io.Externalizable;
import java.io.Serializable;
import java.math.BigInteger;

public class MessageLookup extends Message implements Serializable{


    BigInteger targetId;
    /**
     * Finds out the successor of a node.
     * @param ip Ip of the sender.
     * @param port Port of the sender.
     * @param targetId Id of which we wanna find out the successor.
     */
    public MessageLookup(InfoNode originNode, BigInteger targetId, MessageType type){
        super(originNode, type);
        this.targetId = targetId;
    }

    public InfoNode getOriginNode(){
        return originNode;
    }
    
    public BigInteger getTargetId(){
        return targetId;
    }
}