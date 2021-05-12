package network.message;

import network.node.InfoNode;

public class MessageGetPredecessor extends Message{

    public MessageGetPredecessor(InfoNode originNode) {
        super(originNode, "getPredecessor");
    }
}
