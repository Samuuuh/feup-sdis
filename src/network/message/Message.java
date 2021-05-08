package network.message;

import network.node.InfoNode;

import java.io.Serializable;

public abstract class Message implements Serializable {

    InfoNode originNode;
    String type;

    public Message(InfoNode originNode, String type){
        this.originNode = originNode;
        this.type = type;
    }

    public String getIpOrigin(){
        return originNode.getIp();
    }

    public int getPortOrigin(){
        return originNode.getPort();
    }

    public String getType(){
        return type;
    }


}
