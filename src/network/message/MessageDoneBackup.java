package network.message;

import network.etc.MessageType;
import network.message.Message;
import network.node.InfoNode;

public class MessageDoneBackup extends Message {
    int desiredRepDeg;
    int actualRepDeg;
    String filePath;
    public MessageDoneBackup(InfoNode originNode, MessageType type, int desiredRepDeg, int actualRepDeg, String filePath){
        super(originNode, type);
        this.desiredRepDeg = desiredRepDeg;
        this.actualRepDeg = actualRepDeg;
        this.filePath = filePath;
    }

    public int getDesiredRepDeg() {
        return desiredRepDeg;
    }

    public int getActualRepDeg() {
        return actualRepDeg;
    }

    public String getFilePath(){
        return filePath;
    }
}
