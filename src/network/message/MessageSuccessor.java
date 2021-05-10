package network.message;

import network.node.InfoNode;

import java.io.Serializable;

public class MessageSuccessor extends Message implements Serializable {

    InfoNode successor;

    public MessageSuccessor(InfoNode originNode, InfoNode successor) {
        super(originNode, "successor");
        this.successor = successor;
    }

    public InfoNode getSuccessor(){
        return successor;
    }


}
