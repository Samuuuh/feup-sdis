package network.server.stabilize;

import network.Main;
import network.etc.Logger;
import network.message.MessageGetPredecessor;
import network.node.InfoNode;
import network.server.com.SendMessage;

import java.util.Objects;

/**
 * This class is the beginning of the stabilize process where it tries to find
 * out what is the successor predecessor.
 * https://pdos.csail.mit.edu/papers/ton:chord/paper-ton.pdf
 */
public class GetPredecessor implements Runnable {

    @Override
    public void run() {
        try {
            if (Main.chordNode == null) return;

            InfoNode successor = Main.chordNode.getSuccessor();
            if (Objects.isNull(successor)) return;

            // Case the successor is the own node, there is no necessity of sending the message.
            if (successor == Main.chordNode.getInfoNode()) {
                Main.threadPool.execute(new Stabilize());
                return;
            }

            MessageGetPredecessor messageGetPredecessor = new MessageGetPredecessor(Main.chordNode.getInfoNode());
            Main.threadPool.submit(new SendMessage(successor.getIp(), successor.getPort(), messageGetPredecessor));

        }catch(Exception e){
            e.printStackTrace();
            Logger.ERR(this.getClass().getName(), "Not possible to get predecessor.");
        }

    }


}
