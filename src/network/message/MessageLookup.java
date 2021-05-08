package network.message;

import java.math.BigInteger;

public class MessageLookup extends Message{


    BigInteger targetId;
    /**
     * Finds out the successor of a node.
     * @param ip Ip of the sender.
     * @param port Port of the sender.
     * @param targetId Id of which we wanna find out the successor.
     */
    public MessageLookup(String ip, int port, BigInteger targetId){
        super(ip, port, "lookup");
        this.targetId = targetId;
    }
}