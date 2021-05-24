package network.server;

import network.Main;
import network.message.OK;
import network.node.InfoNode;
import network.server.com.SendMessage;

/**
 * Checks if the predecessor still exists. Case not it will set to null.
 */
public class CheckPredecessor implements Runnable{


    @Override
    public void run() {
        try {
            InfoNode predecessor = Main.chordNode.getPredecessor();
            Main.threadPool.execute(new SendMessage(predecessor.getIp(), predecessor.getPort(), new OK()));
        }catch(Exception e){
               Main.chordNode.setPredecessor(null);
        }
    }
}
