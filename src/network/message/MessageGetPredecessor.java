package network.message;

import network.etc.MessageType;
import network.node.InfoNode;

public class MessageGetPredecessor extends Message{

    public MessageGetPredecessor(InfoNode originNode) {
        super(originNode, MessageType.GET_PREDECESSOR);
    }
}
