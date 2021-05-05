package service.client;

import service.Connection;
import service.message.Message;

import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import java.io.*;
import java.net.InetAddress;

public class SSLConnection implements Connection {

    private final SSLSocket sslSocket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    public SSLConnection(InetAddress ip, int port) throws IOException {

        SSLSocketFactory sslSocketFactory = (SSLSocketFactory) SSLSocketFactory.getDefault();
        this.sslSocket = (SSLSocket) sslSocketFactory.createSocket(ip, port);

        this.out = new ObjectOutputStream(sslSocket.getOutputStream());
        this.in = new ObjectInputStream(sslSocket.getInputStream());
    }

    @Override
    public int getPort() {
        return sslSocket.getPort();
    }

    @Override
    public InetAddress getIp() {
        return sslSocket.getInetAddress();
    }

    public void sendMessage(Message message) throws IOException {
        out.writeObject(message);
    }

    public Message readMessage() throws IOException, ClassNotFoundException {
        return (Message) in.readObject();
    }


}
