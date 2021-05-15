package network.message;

import network.node.InfoNode;
import network.etc.MessageType;

public class MessageRestore extends Message {
    public MessageRestore(InfoNode originNode) {
        super(originNode, MessageType.RESTORE);
      }
}
