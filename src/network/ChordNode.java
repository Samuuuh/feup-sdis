package network;

import network.etc.Logger;
import network.etc.MessageType;
import network.etc.Singleton;
import network.message.*;
import network.node.*;
import network.server.CheckPredecessor.CheckPredecessor;
import network.server.CheckPredecessor.CheckPredecessorOrchestrator;
import network.server.com.*;
import network.server.com.SendMessage;
import network.server.fixFingers.FixFingerOrchestrator;
import network.server.stabilize.GetPredecessor;

import java.io.IOException;
import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;
import java.util.concurrent.*;

/**
 * This is the most important class of the chord system.
 * It's responsible for initialize the Chord Server and contains all the information regarding
 * this chord of this peer.
 */
public class ChordNode implements Serializable {
    // TODO: add linked list.
    private ConcurrentHashMap<BigInteger, InfoNode> fingerTable;
    private ConcurrentLinkedDeque<BigInteger> fingerTableOrder;

    private Integer next;
    private InfoNode successor;
    private InfoNode predecessor;
    private InfoNode infoNode;
    public static ChordServer server;

    public void initNetworkChannel() {
        server = new ChordServer(infoNode.getIp(), infoNode.getPort());
        server.start();
    }

    /**
     * First node of the network.
     *
     * @param infoNode Information about the own node.
     */
    public ChordNode(InfoNode infoNode) {
        fingerTable = new ConcurrentHashMap<>();
        fingerTableOrder = new ConcurrentLinkedDeque<>();
        this.infoNode = infoNode;
        this.predecessor = infoNode;
        this.successor = infoNode;
        this.next = 0;

        initOrderedTable();
        initNetworkChannel();
        Logger.ANY(this.getClass().getName(), "ID: " + infoNode.getId());
        initPeriodicFunctions();

    }

    /**
     * It 's not the first node of the network. It receives another node.d
     *
     * @param randomNode Other node of the network.
     * @param infoNode   Information about the own node.
     */
    public ChordNode(InfoNode infoNode, InfoNode randomNode) {
        try {
            fingerTable = new ConcurrentHashMap<>();
            fingerTableOrder = new ConcurrentLinkedDeque<>();
            this.infoNode = infoNode;
            this.next = 0;
            initOrderedTable();
            initNetworkChannel();

            Logger.ANY(this.getClass().getName(), "ID: " + infoNode.getId());
            lookup(infoNode, randomNode, infoNode.getId());
            initPeriodicFunctions();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void initPeriodicFunctions() {
        // Stabilize
        Main.schedulerPool.scheduleWithFixedDelay(new GetPredecessor(), 0, Singleton.STABILIZE_TIME * 1000L, TimeUnit.MILLISECONDS);
        // Check predecessor
        Main.schedulerPool.schedule(new CheckPredecessorOrchestrator(), 0, TimeUnit.MILLISECONDS);
        // Schedule the fix fingers.
        Main.schedulerPool.scheduleWithFixedDelay(new FixFingerOrchestrator(), 0, Singleton.FIX_FINGERS_TIME * 1000L, TimeUnit.MILLISECONDS);
    }

    public void lookup(InfoNode originNode, InfoNode randomNode, BigInteger targetId) throws IOException, ClassNotFoundException {
        MessageLookup message = new MessageLookup(originNode, targetId, MessageType.LOOKUP);
        new SendMessage(randomNode.getIp(), randomNode.getPort(), message).call();
    }

    public ConcurrentHashMap<BigInteger, InfoNode> getFingerTable() {
        return fingerTable;
    }


    public ConcurrentLinkedDeque<BigInteger> getFingerTableOrder() {
        return fingerTableOrder;
    }

    public InfoNode getSuccessor() {
        return successor;
    }

    public InfoNode getPredecessor() {
        return predecessor;
    }

    public BigInteger getId() {
        return infoNode.getId();
    }

    public InfoNode getInfoNode() {
        return infoNode;
    }

    public Integer getNext() {
        return next;
    }


    public void setNext(Integer next) {
        this.next = next;
    }

    public void setSuccessor(InfoNode successor) {
        if (successor != this.successor)
            Logger.ANY(this.getClass().getName(), "New successor " + successor.getId());

        this.successor = successor;
        fingerTable.put(successor.getId(), successor);
    }


    /**
     * TODO: where are the cases that we need to set this function?
     * Case it's not possible to communicate with the successor, the
     * it's necessary to find a successor in the finger table that is not
     * the actual successor.
     */
    public void fixSuccessor() {
        BigInteger[] fingerTableOrderArray = fingerTableOrder.toArray(new BigInteger[0]);
        for (BigInteger key : fingerTableOrderArray) {
            if (!fingerTable.get(key).getId().equals(successor.getId()))
                setSuccessor(fingerTable.get(key));
        }
        // The only node in the network.
        setSuccessor(infoNode);
    }


    public void setPredecessor(InfoNode predecessor) {
        this.predecessor = predecessor;

        if (predecessor != null)
            Logger.ANY(this.getClass().getName(), "New predecessor " + predecessor.getId());
    }

    /**
     * Receives notification where the node inside MessageInfoNode is a candidate to be the predecessor.
     *
     * @param messageInfoNode - Message received from a node.
     */
    public void notify(MessageInfoNode messageInfoNode) {
        InfoNode notifierInfoNode = messageInfoNode.getInfoNode();
        BigInteger notifierId = notifierInfoNode.getId();

        if (Objects.isNull(this.predecessor) || Singleton.betweenSuccessor(notifierId, predecessor.getId(), this.getId()))
            setPredecessor(notifierInfoNode);

    }


    public void initOrderedTable() {

        for (int i = 0; i < Singleton.m; i++) {
            BigInteger offset = new BigInteger(String.valueOf((long) Math.pow(2, i)));
            BigInteger fingerTableEntry = this.infoNode.getId().add(offset);
            fingerTableOrder.addLast(fingerTableEntry);
        }
    }


    public void printHashTable() {
        System.out.println("---- FINGER TABLE ---- ");
        fingerTable.forEach((key, value) -> {
            System.out.println("KEY: " + key);
            System.out.println("SUCCESSOR: " + value.getId());
        });
        System.out.println("---- END ----");
    }

    public void printFingerTableOrder() {
        System.out.println("---- FINGER ORDER ---- ");
        fingerTableOrder.forEach((key) -> {
            System.out.println("ID: " + key);
        });
        System.out.println("---- END ----");
    }

}
