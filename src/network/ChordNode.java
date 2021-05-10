package network;

import network.etc.Logger;
import network.message.*;
import network.node.*;
import network.server.com.*;
import network.server.com.SendMessage;

import java.math.BigInteger;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

/**
 * This is the most important class of the chord system.
 * It's responsible for initialize the Chord Server and contains all the information regarding
 * this chord of this peer.
 */
public class ChordNode {
    // TODO: add linked list.
    private ConcurrentHashMap<BigInteger, InfoNode> fingerTable;
    private ConcurrentLinkedDeque<BigInteger> fingerTableOrder;

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
        predecessor = null;
        this.infoNode = infoNode;
        this.successor = infoNode;      // The first element of the network is it's own successor.
        initNetworkChannel();
    }

    /**
     * It 's not the first node of the network. It receives another node.d
     * @param randomNode Other node of the network.
     * @param infoNode Information about the own node.
     */
    public ChordNode(InfoNode infoNode, InfoNode randomNode ) {
        fingerTable = new ConcurrentHashMap<>();
        fingerTableOrder = new ConcurrentLinkedDeque<>();
        predecessor = null;
        successor = null;
        this.infoNode = infoNode;
        initNetworkChannel();
        lookup(infoNode, randomNode, infoNode.getId());
    }


    public void lookup(InfoNode originNode, InfoNode randomNode , BigInteger targetId){
        MessageLookup message = new MessageLookup(originNode, targetId);
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

    public InfoNode getInfoNode(){
        return infoNode;
    }

    public void addSuccessor(InfoNode successor){
        this.successor = successor;
        // TODO: add this to the fingerTableOrder and reorder it.
        fingerTableOrder.add(successor.getId());
        // TODO: ask professor with we need to add it to the hash table
        fingerTable.put(successor.getId(), successor);
        Logger.ANY(this.getClass().getName(), "Added the node to the successors");
    }


}
