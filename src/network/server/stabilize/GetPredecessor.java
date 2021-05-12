package network.server.stabilize;

import network.Main;
import network.message.MessageGetPredecessor;
import network.node.InfoNode;
import network.server.com.SendMessage;

import java.util.Objects;

public class GetPredecessor implements Runnable {
    @Override
    public void run() {
        InfoNode successor = Main.chordNode.getSuccessor();

        if (Objects.isNull(successor)) return;

        // Case the successor is the own node, there is no necessity of sending the message.
        if (successor == Main.chordNode.getInfoNode()) {
            Main.threadPool.execute(new Stabilize());
            return;
        }

        MessageGetPredecessor messageGetPredecessor = new MessageGetPredecessor(Main.chordNode.getInfoNode());
        Main.threadPool.execute(new SendMessage(successor.getIp(),successor.getPort(), messageGetPredecessor));
    }


}
