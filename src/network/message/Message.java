package network.message;

import network.etc.MessageType;
import network.node.InfoNode;

import java.io.Serializable;

public abstract class Message implements Serializable {

    InfoNode originNode;
    MessageType type;

    public Message(InfoNode originNode, MessageType type){
        this.originNode = originNode;
        this.type = type;
    }

    public InfoNode getOriginNode() {
        return originNode;
    }

    public String getIpOrigin(){
        return originNode.getIp();
    }

    public int getPortOrigin(){
        return originNode.getPort();
    }

    public MessageType getType(){
        return type;
    }


}
