package network.message;

import network.Main;
import network.etc.MessageType;

import java.io.Serializable;


public class OK extends Message implements Serializable {

    public OK() {
        super(Main.chordNode.getInfoNode(), MessageType.OK);
    }
}
