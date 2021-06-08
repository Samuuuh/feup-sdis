package network.message;

import network.etc.MessageType;
import network.node.InfoNode;

/**
 * This message says that a certain peer has stored the file.
 * TODO: filePath neeeds to be the hash.
 */
public class MessageStored extends Message{
    String filePath;
    public MessageStored(InfoNode originNode, MessageType type, String filePath){
        super(originNode, type);
        this.filePath = filePath;
    }

    public String getFilePath(){
        return filePath;
    }
}
