package network.message;

import network.etc.MessageType;
import network.node.InfoNode;

import java.math.BigInteger;

public class MessageSuccessor extends Message {
    BigInteger targetId;
    InfoNode successor;


    public MessageSuccessor(InfoNode originNode, BigInteger targetId, InfoNode successor, MessageType type){
        super(originNode, type);
        this.successor = successor;
        this.targetId = targetId;
    }

    public BigInteger getTargetId() {
        return targetId;
    }

    public InfoNode getSuccessor() {
        return successor;
    }

}
