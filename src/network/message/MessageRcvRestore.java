package network.message;

import network.etc.MessageType;
import network.node.InfoNode;

public class MessageRcvRestore extends Message {
    byte[] byteFile;
    String filePath;
    String hash;
    public MessageRcvRestore(InfoNode originNode, String hash, byte[] byteFile, String filePath) {
        super(originNode, MessageType.RCV_RESTORE);
        this.hash = hash;
        this.byteFile = byteFile;
        this.filePath = filePath;

    }

    public String getHash(){
        return hash;
    }

    public byte[] getBytes() {
        return byteFile;
    }

    public String getFileName() {
        return filePath;
    }
}