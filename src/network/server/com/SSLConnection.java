package network.server.com;

import network.message.*;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.InetAddress;

public class SSLConnection implements Connection{
    private final SSLSocket sslSocket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    /**
     * SSL Connection class constructor
     * @param ip ip of the connection
     * @param port port of the connection
     */
    public SSLConnection(InetAddress ip, int port) throws IOException {
        SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();

        this.sslSocket = (SSLSocket) sslSocketFactory.createSocket(ip, port);

        this.out = new ObjectOutputStream(sslSocket.getOutputStream());
        this.in = new ObjectInputStream(sslSocket.getInputStream());
    }

    /**
     * Accept the sslsocket 
     */
    public SSLSocket accept() {
        return sslSocket;
    }

    /**
     * Get the port of socket
     * @return int return port
     */
    @Override
    public int getPort() {
        return sslSocket.getPort();
    }

    /**
     * Get the ip of socket
     * @return the ip of socket
     */
    @Override
    public InetAddress getIp() {
        return sslSocket.getInetAddress();
    }

    /**
    * Send message
    * @param message messageto be sent
     */
    public void sendMessage(Message message) throws IOException {
        out.writeObject(message);
    }

    /**
     * Read message
     */
    public Message readMessage() throws IOException, ClassNotFoundException {
        return (Message) in.readObject();
    }

    /**
     * Close out
     */
    public void closeOut() throws IOException {
        out.close();
    }

    /**
     * Close in 
     */
    public void closeIn() throws IOException {
        in.close();
    }

    /**
     * Close the socket
    */
    public void closeSocket() throws IOException {
        this.sslSocket.close();
    }
}
