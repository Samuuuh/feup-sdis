package network.node;

import network.etc.*;

import java.io.Serializable;
import java.math.BigInteger;

public class InfoNode implements Serializable {

    BigInteger id;
    String ip;
    int port;


    public InfoNode(String ip,int port,  BigInteger id ){
        this.ip = ip;
        this.id = id;
        this.port = port;
    }

    public InfoNode(String ip, int port){
        this.port = port;
        this.ip = ip;
        String uncodedId = Singleton.getIdUncoded(ip, port);
        id = Singleton.encode(uncodedId);
    }

    public BigInteger getId(){
        return id;
    }

    public String getIp() {
        return ip;
    }

    public int getPort() {
        return port;
    }


}
