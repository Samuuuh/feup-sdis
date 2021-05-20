package network.message;

import network.etc.MessageType;
import network.node.InfoNode;

public class MessageRcvRestore extends Message {
    byte[] byteFile;
    String filePath;

    public MessageRcvRestore(InfoNode originNode, byte[] byteFile, String filePath) {
        super(originNode, MessageType.RCV_RESTORE);
        
        this.byteFile = byteFile;
        this.filePath = filePath;
    }

    public byte[] getBytes() {
        return byteFile;
    }

    public String getFileName() {
        return filePath;
    }
}