package network.message;

import network.Main;
import network.etc.MessageType;

public class OK extends Message {

    public OK() {
        super(Main.chordNode.getInfoNode(), MessageType.OK);
    }
}
