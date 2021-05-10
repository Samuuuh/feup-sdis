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
            System.out.println("Send message -> " + port);
            InetAddress host = InetAddress.getByName("127.0.0.1");
            this.connectionSocket = new SSLConnection(host, port);
            this.message = message;
            System.out.println("SendMessage constructor done");
        }catch(IOException e){
            e.printStackTrace();
            Logger.ERR(this.getClass().getName(), "Not possible to initialize SSLSocket");
        }
    }

    @Override
    public void run() {
        try {
            System.out.println("Before run Thread");
            System.out.println(this.message.getClass());
            this.connectionSocket.sendMessage(this.message);
            this.connectionSocket.readMessage();
            System.out.println("After run Thread");
        }
        catch(Exception e) {
            e.printStackTrace();
            System.out.println("ERROR");
        }
    }

}
