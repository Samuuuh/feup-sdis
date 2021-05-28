package network.message.reclaim;

import network.etc.MessageType;
import network.etc.Singleton;
import network.message.Message;
import network.node.InfoNode;

import java.math.BigInteger;

public class MessageReclaim extends Message {
    int size;
    BigInteger targetId;
    int messageId;

    public MessageReclaim(InfoNode originNode, MessageType type, BigInteger targetId, int size) {
        super(originNode, type);
        this.targetId = targetId;
        this.size = size;
        this.messageId = (targetId + " " + size).hashCode();
    }

    public int getSize(){
        return size;
    }

    public BigInteger getTargetId(){
        return targetId;
    }

    public int getMessageId() {
        return messageId;
    }
}
