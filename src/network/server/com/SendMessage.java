package network.server.com;

import network.etc.*;
import network.message.*;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.Callable;

public class SendMessage implements Callable {
    SSLConnection connectionSocket;
    Message message;

    public SendMessage(String ip, int port, Message message) throws IOException {
        InetAddress host = InetAddress.getByName(ip);
        this.connectionSocket = new SSLConnection(host, port);
        this.message = message;

    }

    @Override
    public Boolean call() throws IOException, ClassNotFoundException {
        this.connectionSocket.sendMessage(this.message);
        this.connectionSocket.readMessage();
        return true;
    }

}
