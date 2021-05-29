package network.server.com;

import java.net.InetAddress;

public interface Connection {
    /**
    * Get the ip of connection
    * @return int ip of connection 
    */
    InetAddress getIp();

    /**
    * Get the port of connection
    * @return int port number 
    */
    int getPort();
}
