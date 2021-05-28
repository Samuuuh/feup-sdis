package network.message;

import network.etc.MessageType;
import network.message.Message;
import network.node.InfoNode;

public class MessageBackup extends Message {
  byte[] byteFiles;
  String fileName;
  int desiredRepDeg;
  int actualRepDeg;

  public MessageBackup(InfoNode originNode, String fileName, byte[] byteFiles, int desiredRepDeg, int actualRepDeg) {
      super(originNode, MessageType.BACKUP);
      this.byteFiles = byteFiles;
      this.fileName = fileName;
      this.desiredRepDeg = desiredRepDeg;
      this.actualRepDeg = actualRepDeg;
  }

  public byte[] getBytes() {
      return byteFiles;
  }

  public String getFileName() {
      return fileName;
  }

  public int getDesiredRepDeg() {
      return desiredRepDeg;
  }

  public int getActualRepDeg() {
      return actualRepDeg;
  }
}
