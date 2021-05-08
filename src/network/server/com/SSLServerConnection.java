package network.server.com;


import network.Connection;

import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.InetAddress;

public class SSLServerConnection implements Connection {
    private InetAddress ip;
    private final SSLServerSocket sslServerSocket;

    public SSLServerConnection(int port) throws IOException {

        SSLServerSocketFactory sslServerSocketFactory = (SSLServerSocketFactory) SSLServerSocketFactory.getDefault();
        sslServerSocket = (SSLServerSocket) sslServerSocketFactory.createServerSocket(port);
    }


    public SSLSocket accept() throws IOException {
        return (SSLSocket) sslServerSocket.accept();
    }


    public int getPort(){
        return sslServerSocket.getLocalPort();
    }

    public InetAddress getIp(){
        return ip;
    }



}
