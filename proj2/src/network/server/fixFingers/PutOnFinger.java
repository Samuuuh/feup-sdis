package network.server.fixFingers;

import network.Main;
import network.message.MessageSuccessor;

public class PutOnFinger implements Runnable {
    MessageSuccessor message;

    /**
    * Responsible for put the received finger in the table.
    * @param messageSuccessor received finger
    */
    public PutOnFinger(MessageSuccessor messageSuccessor){
        this.message = messageSuccessor;
    }

    /**
    * Put element on finger table
    */
    @Override
    public void run() {
        var fingerTable = Main.chordNode.getFingerTable();
        fingerTable.put(this.message.getTargetId(), this.message.getSuccessor());
    }
}
