package network.message;

import network.etc.MessageType;
import network.node.InfoNode;

public class MessageDelete extends Message {
    String filePath;
    
    public MessageDelete(InfoNode originNode, String filePath) {
        super(originNode, MessageType.DELETE);
        this.filePath = filePath;
    }

    public String getFilePath() {
        return filePath;
    }
}
