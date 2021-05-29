package network.server.com;

import network.etc.*;
import network.message.*;

import java.io.IOException;
import java.net.InetAddress;
import java.util.concurrent.Callable;

public class SendMessage implements Callable {
    SSLConnection connectionSocket;
    Message message;

    /**
    * Send Message Constructor
    * @param ip ip of the connection to be established
    * @param port port of the connection to be established
    */
    public SendMessage(String ip, int port, Message message) throws IOException {
        InetAddress host = InetAddress.getByName(ip);
        this.connectionSocket = new SSLConnection(host, port);
        this.message = message;

    }

    /**
     * Send the message
     */
    @Override
    public Boolean call() throws IOException, ClassNotFoundException {
        this.connectionSocket.sendMessage(this.message);
        this.connectionSocket.readMessage();
        return true;
    }

}
