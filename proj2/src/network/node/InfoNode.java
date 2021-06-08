package network.node;

import network.etc.*;

import java.io.Serializable;
import java.math.BigInteger;

public class InfoNode implements Serializable {
    BigInteger id;
    String ip;
    int port;

    /**
     * Class constructor.
     * @param ip Ip address of the peer
     * @param port Port of the peer
     * @param id Unique identifier of the peer on chord
     */
    public InfoNode(String ip, int port, BigInteger id) {
        this.ip = ip;
        this.id = id;
        this.port = port;
    }

    /**
     * Class constructor.
     * @param ip Ip address of the peer
     * @param port Port of the peer
     */
    public InfoNode(String ip, int port) {
        this.port = port;
        this.ip = ip;
        String uncodedId = Singleton.getIdUncoded(ip, port);
        id = Singleton.encode(uncodedId);
    }

    /**
     * Get the Id of the peer
     * @return BigInteger Id of the peer
     */
    public BigInteger getId(){
        return id;
    }

     /**
     * Get the Ip of the peer
     * @return String Ip of the peer
     */
    public String getIp() {
        return ip;
    }

     /**
     * Get the Port of the peer
     * @return int Port of the peer
     */
    public int getPort() {
        return port;
    }
}
