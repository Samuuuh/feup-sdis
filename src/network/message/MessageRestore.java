package network.message;

import network.node.InfoNode;
import network.etc.MessageType;

public class MessageRestore extends Message {
    String fileName;

    public MessageRestore(InfoNode originNode, String fileName) {
      super(originNode, MessageType.RESTORE);

      this.fileName = fileName;
    }

    public String getFileName() {
      return fileName;
    }
}
