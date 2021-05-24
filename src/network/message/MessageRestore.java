package network.message;

import network.node.InfoNode;
import network.etc.MessageType;

public class MessageRestore extends Message {
    String filePath;

    public MessageRestore(InfoNode originNode, String filePath) {
      super(originNode, MessageType.RESTORE);

      this.filePath = filePath;
    }

    public String getFile() {
      return filePath;
    }
}
