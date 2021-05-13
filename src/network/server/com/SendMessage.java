package network.server.com;

import network.etc.*;
import network.message.*;

import java.io.IOException;
import java.net.InetAddress;

public class SendMessage extends Thread {
    SSLConnection connectionSocket;

    Message message;

    public SendMessage(String ip, int port, Message message) {
        try {
            InetAddress host = InetAddress.getByName("127.0.0.1");
            this.connectionSocket = new SSLConnection(host, port);
            this.message = message;
        }catch(IOException e){
            // What if there is an error.
            e.printStackTrace();
            Logger.ERR(this.getClass().getName(), "Not possible to initialize SSLSocket");
        }
    }

    @Override
    public void run() {
        try {
            this.connectionSocket.sendMessage(this.message);
            this.connectionSocket.readMessage();
        }
        catch(Exception e) {
            e.printStackTrace();
            System.out.println("ERROR");
        }
    }

}
