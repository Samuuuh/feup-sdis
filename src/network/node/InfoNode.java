package network.node;

import java.math.BigInteger;

public class InfoNode {

    BigInteger id;
    String ip;
    int port;

    public InfoNode(BigInteger id, int port, String ip){
        this.ip = ip;
        this.id = id;
        this.port = port;
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
