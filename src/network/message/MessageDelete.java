package network.message;

import network.etc.MessageType;
import network.node.InfoNode;

public class MessageDelete extends Message {
    String fileName;
    
    public MessageDelete(InfoNode originNode, String fileName) {
        super(originNode, MessageType.DELETE);
        this.fileName = fileName;
    }

    public String getFileName() {
        return fileName;
    }
}
