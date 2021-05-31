package network.message;

import network.etc.MessageType;
import network.etc.Singleton;
import network.node.InfoNode;

public class MessageBackup extends Message {
  byte[] byteFiles;
  String fileName;
  String hash;
  int desiredRepDeg;
  int actualRepDeg;

  public MessageBackup(InfoNode originNode, String filePath, byte[] byteFiles, int desiredRepDeg, int actualRepDeg) {
      super(originNode, MessageType.BACKUP);
      this.byteFiles = byteFiles;
      this.fileName = Singleton.getFileName(filePath) + "." + Singleton.getFileExtension(filePath);
      this.hash = Singleton.hash(this.fileName);
      this.desiredRepDeg = desiredRepDeg;
      this.actualRepDeg = actualRepDeg;
  }

  public byte[] getBytes() {
      return byteFiles;
  }

  public String getFileName() {
      return fileName;
  }

  public String getHash() {
      return hash;
  }

  public int getDesiredRepDeg() {
      return desiredRepDeg;
  }

  public int getActualRepDeg() {
      return actualRepDeg;
  }
}
