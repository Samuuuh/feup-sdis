package network.message;

import network.etc.MessageType;
import network.node.InfoNode;

public class MessageInfoNode extends Message {

    InfoNode infoNode;

    public MessageInfoNode(InfoNode originNode, MessageType type, InfoNode infoNode){
        super(originNode, type);
        this.infoNode = infoNode;
    }

    public InfoNode getInfoNode(){
        return infoNode;
    }
}
