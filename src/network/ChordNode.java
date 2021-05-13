package network;

import network.etc.Logger;
import network.etc.MessageType;
import network.etc.Singleton;
import network.message.*;
import network.node.*;
import network.server.com.*;
import network.server.com.SendMessage;
import network.server.stabilize.GetPredecessor;

import java.io.Serializable;
import java.math.BigInteger;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.TimeUnit;

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
     * @param infoNode Information about the own node.
     */
    public ChordNode(InfoNode infoNode) {
        fingerTable = new ConcurrentHashMap<>();
        fingerTableOrder = new ConcurrentLinkedDeque<>();
        this.infoNode = infoNode;
        this.predecessor = infoNode;
        this.successor = infoNode;
        this.next = 0;
        initNetworkChannel();
        Logger.ANY(this.getClass().getName(), "ID: " + infoNode.getId());
        // TODO: thread pool
        Main.schedulerPool.scheduleWithFixedDelay(new GetPredecessor(), 0, Singleton.STABILIZE_TIME* 1000L, TimeUnit.MILLISECONDS);
    }

    /**
     * It 's not the first node of the network. It receives another node.d
     * @param randomNode Other node of the network.
     * @param infoNode Information about the own node.
     */
    public ChordNode(InfoNode infoNode, InfoNode randomNode ) {
        fingerTable = new ConcurrentHashMap<>();
        fingerTableOrder = new ConcurrentLinkedDeque<>();
        this.infoNode = infoNode;
        initNetworkChannel();
        this.next = 0;
        Logger.ANY(this.getClass().getName(), "ID: " + infoNode.getId());
        lookup(infoNode, randomNode, infoNode.getId());
        Main.schedulerPool.scheduleWithFixedDelay(new GetPredecessor(), 0, Singleton.STABILIZE_TIME* 1000L, TimeUnit.MILLISECONDS);
    }


    public void lookup(InfoNode originNode, InfoNode randomNode , BigInteger targetId){
        MessageLookup message = new MessageLookup(originNode, targetId, MessageType.LOOKUP);
        new SendMessage(randomNode.getIp(), randomNode.getPort(), message).start();
    }

    public ConcurrentHashMap<BigInteger, InfoNode> getFingerTable(){
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

    public BigInteger getId(){
        return infoNode.getId();
    }

    public InfoNode getInfoNode(){
        return infoNode;
    }

    public Integer getNext(){
        return next;
    }

    public void setNext(Integer next){
        this.next = next;
    }
    public void setSuccessor(InfoNode successor){
        if (successor != this.successor)
            Logger.ANY(this.getClass().getName(), "New successor " + successor.getId());

        this.successor = successor;
        // TODO: is this necessary?
        fingerTableOrder.add(successor.getId());
        fingerTable.put(successor.getId(), successor);
    }


    public void setPredecessor(InfoNode predecessor){
        Logger.ANY(this.getClass().getName(), "New predecessor " + predecessor.getId());
        this.predecessor = predecessor;
    }

    /**
     * Receives notification where the node inside MessageInfoNode is a candidate to be the predecessor.
     * @param messageInfoNode - Message received from a node.
     */
    public void notify(MessageInfoNode messageInfoNode){
        InfoNode notifierInfoNode = messageInfoNode.getInfoNode();
        BigInteger notifierId = notifierInfoNode.getId();

        if (Objects.isNull(this.predecessor) || Singleton.betweenSuccessor(notifierId, predecessor.getId(), this.getId()))
            setPredecessor(notifierInfoNode);

    }




}
