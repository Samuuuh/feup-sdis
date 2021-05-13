package network.server.fixFingers;


import network.Main;
import network.message.MessageLookup;

/**
 * Class responsible for put the received finger in the table.
 */
public class PutOnFinger implements Runnable {

    MessageLookup message;
    public PutOnFinger(MessageLookup messageLookup){
        this.message = messageLookup;
    }


    @Override
    public void run() {
        var fingerTable = Main.chordNode.getFingerTable();
    }
}
