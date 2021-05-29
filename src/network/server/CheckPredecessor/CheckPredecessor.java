package network.server.CheckPredecessor;

import network.Main;
import network.message.OK;
import network.node.InfoNode;
import network.server.com.SendMessage;

/**
 * Checks if the predecessor still exists. Case not it will set to null.
 */
public class CheckPredecessor implements Runnable {
    @Override
    public void run() {
        try {
            InfoNode predecessor = Main.chordNode.getPredecessor();
            new SendMessage(predecessor.getIp(), predecessor.getPort(), new OK()).call();
        }catch(Exception e){
            Main.chordNode.setPredecessor(null);
        }
    }

}
