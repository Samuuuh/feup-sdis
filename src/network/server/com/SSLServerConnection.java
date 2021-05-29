package network.server.com;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.InetAddress;

public class SSLServerConnection implements Connection {
    private InetAddress ip;
    private final SSLServerSocket sslServerSocket;

    /**
     * SSL Server Connection 
     * @param port
     */
    public SSLServerConnection(int port) throws IOException {
        System.out.println("New ServerConnection on port " + port);
        SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        
        sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(port);
    }

    /**
     * Accept SSLSocket
     */
    public SSLSocket accept() throws IOException {
        return (SSLSocket) sslServerSocket.accept();
    }

    /**
     * Get Port of ssl Server
     * @return int port of ssl server
     */
    public int getPort() {
        return sslServerSocket.getLocalPort();
    }

    /**
     * Get the ip of ssl server
     * @return InetAddress ip 
     */
    public InetAddress getIp(){
        return ip;
    }
}
