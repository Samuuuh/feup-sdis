package network.server.stabilize;

import network.Main;
import network.etc.Logger;
import network.etc.Singleton;
import network.message.MessageGetPredecessor;
import network.node.InfoNode;
import network.server.com.SendMessage;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

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
            Logger.ERR(this.getClass().getName(), "Error on GetPredecessor.");
            Main.chordNode.setSuccessor(Main.chordNode.getInfoNode());
            Main.schedulerPool.scheduleWithFixedDelay(new GetPredecessor(), 100, Singleton.STABILIZE_TIME * 1000L, TimeUnit.MILLISECONDS);
        }

    }


}
