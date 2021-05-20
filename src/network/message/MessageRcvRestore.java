package network.message;

import network.etc.MessageType;
import network.node.InfoNode;

public class MessageRcvRestore extends Message {
    MessageBackup file;

    public MessageRcvRestore(InfoNode originNode, MessageBackup file) {
      super(originNode, MessageType.RCV_RESTORE);

      this.file = file;
    }

    public MessageBackup getFile() {
      return file;
    }
}