package network.server.stabilize;

import network.Main;
import network.etc.Logger;
import network.etc.MessageType;
import network.etc.Singleton;
import network.message.MessageInfoNode;
import network.node.InfoNode;
import network.server.com.SendMessage;

import java.math.BigInteger;
import java.util.Objects;

/**
 * Once the successor predecessor was found, the stabilize will check if it's
 * the peer successor.
 * https://pdos.csail.mit.edu/papers/ton:chord/paper-ton.pdf
 */
public class Stabilize implements Runnable {
    InfoNode sucPredecessor;

    /**
     * Set the sucessor
     * @param messageInfoNode the successor node
     */
    public Stabilize(MessageInfoNode messageInfoNode) {
        this.sucPredecessor = messageInfoNode.getInfoNode();
    }

    /** 
     * When the successor is himself.
     */
    public Stabilize() {
        this.sucPredecessor = Main.chordNode.getPredecessor();
    }

    @Override
    public void run() {
        try {
            BigInteger currentId = Main.chordNode.getInfoNode().getId();

            if (Objects.isNull(sucPredecessor))
                return;

            if (Singleton.betweenPredecessor(sucPredecessor.getId(), currentId, Main.chordNode.getSuccessor().getId())) {
                Main.chordNode.setSuccessor(sucPredecessor);
            }

            MessageInfoNode message = new MessageInfoNode(Main.chordNode.getInfoNode(), MessageType.NOTIFY, Main.chordNode.getInfoNode());
            new SendMessage(Main.chordNode.getSuccessor().getIp(), Main.chordNode.getSuccessor().getPort(), message).call();

        } catch (Exception e) {
            e.printStackTrace();
            Logger.ERR(this.getClass().getName(), "Error on stabilizing.");
        }
    }
}
