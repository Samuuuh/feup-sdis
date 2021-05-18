package network.message;

import network.etc.MessageType;
import network.node.InfoNode;

/**
 * This message says that a certain peer has stored the file.
 */
public class MessageStored extends Message{

    public MessageStored(InfoNode originNode, MessageType type){
        super(originNode, type);
    }
}
