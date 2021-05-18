package network.message;

import network.etc.MessageType;
import network.node.InfoNode;

public class MessageDoneBackup extends Message {
    int desiredRepDeg;
    int actualRepDeg;
    
    public MessageDoneBackup(InfoNode originNode, MessageType type, int desiredRepDeg, int actualRepDeg){
        super(originNode, type);
        this.desiredRepDeg = desiredRepDeg;
        this.actualRepDeg = actualRepDeg;
    }

    public int getDesiredRepDeg() {
        return desiredRepDeg;
    }

    public int getActualRepDeg() {
        return actualRepDeg;
    }
}
