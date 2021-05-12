package network.message;

import network.ChordNode;
import network.node.InfoNode;

import java.io.Serializable;

public class MessageInfoNode extends Message implements Serializable {

    InfoNode infoNode;

    public MessageInfoNode(InfoNode originNode, InfoNode infoNode) {
        super(originNode, "successor");
        this.infoNode = infoNode;
    }

    public MessageInfoNode(InfoNode originNode, String type, InfoNode infoNode){
        super(originNode, type);
        this.infoNode = infoNode;
    }

    public InfoNode getInfoNode(){
        return infoNode;
    }



}
