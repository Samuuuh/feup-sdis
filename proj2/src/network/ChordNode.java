package network;

import network.etc.Logger;
import network.etc.MessageType;
import network.etc.Singleton;
import network.message.*;
import network.node.*;
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

    /**
     * Init the chord server
     */
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

    /**
     * Init periodic chord functions
     */
    public void initPeriodicFunctions() {
        // Stabilize
        Main.schedulerPool.scheduleWithFixedDelay(new GetPredecessor(), 100, Singleton.STABILIZE_TIME * 1000L, TimeUnit.MILLISECONDS);
        // Check predecessor
        Main.schedulerPool.schedule(new CheckPredecessorOrchestrator(), 5000, TimeUnit.MILLISECONDS);
        // Schedule the fix fingers.
        Main.schedulerPool.scheduleWithFixedDelay(new FixFingerOrchestrator(), 100, Singleton.FIX_FINGERS_TIME * 1000L, TimeUnit.MILLISECONDS);
    }

    /**
     * Lookup chord function
     * @param originNode current nodeInfo
     * @param randomNode know node to send message
     * @param targetId id of the peer in chord
     */
    public void lookup(InfoNode originNode, InfoNode randomNode, BigInteger targetId) throws IOException {
        MessageLookup message = new MessageLookup(originNode, targetId, MessageType.LOOKUP);
        Main.threadPool.submit(new SendMessage(randomNode.getIp(), randomNode.getPort(), message));
    }

     /**
     * Get the Finger Table 
     * @return ConcurrentHashMap<BigInteger, InfoNode> finger table
     */
    public ConcurrentHashMap<BigInteger, InfoNode> getFingerTable() {
        return fingerTable;
    }

    /**
     * Get the Finger Table Order
     * @return ConcurrentLinkedDeque<BigInteger> finger table order
     */
    public ConcurrentLinkedDeque<BigInteger> getFingerTableOrder() {
        return fingerTableOrder;
    }

    /**
     * Get successor of current peer
     * @return InfoNode successor
     */
    public InfoNode getSuccessor() {
        return successor;
    }

    /**
     * Get Predecessor of current peer
     * @return InfoNode predecessor
     */
    public InfoNode getPredecessor() {
        return predecessor;
    }

    /**
     * Get Id of current peer
     * @return BigInteger id
     */
    public BigInteger getId() {
        return infoNode.getId();
    }

    /**
     * Get InfoNode of current peer
     * @return InfoNode get
     */
    public InfoNode getInfoNode() {
        return infoNode;
    }

    /**
     * Get Next
     * @return Integer Next
     */
    public Integer getNext() {
        return next;
    }

    /**
     * Set next
     * @param Integer next id
     */
    public void setNext(Integer next) {
        this.next = next;
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

    /**
     * Set the sucessor of current peer
     * @param sucessor Node information of the successor
     */
    public void setSuccessor(InfoNode successor) {
        if (successor != this.successor)
            Logger.ANY(this.getClass().getName(), "New successor " + successor.getId());

        this.successor = successor;
    }

    /**
     * Set the predecessor of current peer
     * @param predecessor Node information of the predecessor
     */
    public void setPredecessor(InfoNode predecessor) {
        this.predecessor = predecessor;
        if (predecessor != null)
            Logger.ANY(this.getClass().getName(), "New predecessor " + predecessor.getId());
    }

    /**
     * Receives notification where the node inside MessageInfoNode is a candidate to be the predecessor.
     * @param messageInfoNode - Message received from a node.
     */
    public void notify(MessageInfoNode messageInfoNode) {
        InfoNode notifierInfoNode = messageInfoNode.getInfoNode();
        BigInteger notifierId = notifierInfoNode.getId();

        if (Objects.isNull(this.predecessor) || Singleton.betweenSuccessor(notifierId, predecessor.getId(), this.getId()))
            setPredecessor(notifierInfoNode);
    }

    /**
     * Init the finger table
     */
    public void initOrderedTable() {
        for (int i = 0; i < Singleton.m; i++) {
            BigInteger offset = new BigInteger(String.valueOf((long) Math.pow(2, i)));
            BigInteger fingerTableEntry = this.infoNode.getId().add(offset);
            fingerTableOrder.addLast(fingerTableEntry);
        }
    }

    /**
     * Print the Finger Table
     */
    public void printHashTable() {
        System.out.println("---- FINGER TABLE ---- ");
        fingerTable.forEach((key, value) -> {
            System.out.println("KEY: " + key);
            System.out.println("SUCCESSOR: " + value.getId());
        });
        System.out.println("---- END ----");
    }

    /**
     * Print the order of Finger Table 
     */
    public void printFingerTableOrder() {
        System.out.println("---- FINGER ORDER ---- ");
        fingerTableOrder.forEach((key) -> {
            System.out.println("ID: " + key);
        });
        System.out.println("---- END ----");
    }
}
