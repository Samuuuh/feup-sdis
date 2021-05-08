package network.message;

import java.io.Serializable;

public abstract class Message implements Serializable {

    String ip;
    int port;
    String type;

    public Message(String ip, int port, String type){
        this.ip = ip;
        this.port = port;
        this.type = type;
    }

    public String getIp(){
        return ip;
    }

    public int getPort(){
        return port;
    }

    public String getType(){
        return type;
    }


}
