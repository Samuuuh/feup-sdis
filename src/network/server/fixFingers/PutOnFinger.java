package network.server.fixFingers;


import network.Main;
import network.message.MessageSuccessor;

/**
 * Class responsible for put the received finger in the table.
 */
public class PutOnFinger implements Runnable {

    MessageSuccessor message;

    public PutOnFinger(MessageSuccessor messageSuccessor){
        this.message = messageSuccessor;
    }

    @Override
    public void run() {
        var fingerTable = Main.chordNode.getFingerTable();
        fingerTable.put(this.message.getTargetId(), this.message.getSuccessor());
    }
}
