package network.message;

import network.etc.MessageType;
import network.node.InfoNode;

public class MessageBackup extends Message {
  byte[] byteFiles;

  public MessageBackup(InfoNode originNode, byte[] byteFiles) {

    super(originNode, MessageType.BACKUP);
    this.byteFiles = byteFiles;
  }

  public byte[] getBytes() {
    return byteFiles;
  }
}
