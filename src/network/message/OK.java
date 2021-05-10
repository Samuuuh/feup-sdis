package network.message;

import network.Main;

import java.io.Serializable;


public class OK extends Message implements Serializable {

    public OK() {
        super(Main.chordNode.getInfoNode(), "OK");
    }
}
