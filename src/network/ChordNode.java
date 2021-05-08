package network;

import network.node.InfoNode;
import network.server.com.ChordServer;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is the most important class of the chord system.
 * It's responsible for initialize the Chord Server and contains all the information regarding
 * this chord of this peer.
 */
public class ChordNode {
    // TODO: add linked list.
    private ConcurrentHashMap<String, InfoNode> fingerTable;
    private InfoNode successor;
    private InfoNode predecessor;
    private InfoNode infoNode;
    public static ChordServer server;

    public void initNetworkChannel() {
        server = new ChordServer(infoNode.getIp(), infoNode.getPort());
        server.start();
    }

    public ChordNode(InfoNode chordInfo) {
        fingerTable = new ConcurrentHashMap<>();
        predecessor = null;
        successor = null;
        this.infoNode = chordInfo;
        initNetworkChannel();

    }

    public ChordNode(InfoNode node, InfoNode chordInfo ) {
        fingerTable = new ConcurrentHashMap<>();
        predecessor = null;
        successor = node;
        this.infoNode = chordInfo;
        initNetworkChannel();
    }



    public ConcurrentHashMap<String, InfoNode> getFingerTable(){
        return fingerTable;
    }

    public InfoNode getSuccessor() {
        return successor;
    }

    public InfoNode getPredecessor() {
        return predecessor;
    }

    /**
     * Function responsible for finding the successor of a node. Case it's not possible to find the successor,
     * return the closest node id. After this, we must send a message for this node to ask the same question.
     * @param requestId Id of the node that we want to find the successor.
     */
    public InfoNode findSuccessor(BigInteger requestId){
        // id < requestId && requestId < successor.id()
        if (infoNode.getId().compareTo(requestId) > 0 &&  requestId.compareTo(successor.getId()) <= 0)
            return successor;
        else {
            return closestPrecedingNode(requestId);
        }
    }

    public InfoNode closestPrecedingNode(BigInteger requestId) {
        // Store all the nodes in list.
        List<InfoNode> tempNodes = new ArrayList<>();
        fingerTable.forEach((k, v)->{
            tempNodes.add(v);
        });

        for(int i = tempNodes.size() - 1; i > 0; i--) {
            if (requestId.compareTo(tempNodes.get(i).getId()) >= 0 && tempNodes.get(i).getId().compareTo(successor.getId()) <= 0)
                return tempNodes.get(i);
        }
        return infoNode;
    }
}
