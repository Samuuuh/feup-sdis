package network.message;

import network.etc.MessageType;
import network.node.InfoNode;

public class MessageDelete extends Message {
    String hash;
    
    public MessageDelete(InfoNode originNode, String hash) {
        super(originNode, MessageType.DELETE);
        this.hash = hash;
    }

    public String getHash() {
        return hash;
    }
}
