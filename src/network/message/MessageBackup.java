package network.message;

import network.etc.MessageType;
import network.node.InfoNode;

public class MessageBackup extends Message {
  byte[] byteFiles;
  String fileName;

  public MessageBackup(InfoNode originNode, String fileName, byte[] byteFiles) {

    super(originNode, MessageType.BACKUP);
    this.byteFiles = byteFiles;
    this.fileName = fileName;
  }

  public byte[] getBytes() {
    return byteFiles;
  }

  public String getFileName() {
      return fileName;
  }
}
