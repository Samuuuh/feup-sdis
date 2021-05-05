package service;

import service.client.SSLConnection;
import service.message.MessageHello;

import java.io.IOException;
import java.net.InetAddress;

public class Client {

    public static void main(String[] args) throws IOException, ClassNotFoundException, InterruptedException {
        // first is the port the second is the ip

        InetAddress ip = InetAddress.getByName(args[0]);
        int port = Integer.parseInt(args[1]);
        SSLConnection con = new SSLConnection(ip, port);
        MessageHello message = new MessageHello();
        con.sendMessage(message);

        MessageHello response = (MessageHello) con.readMessage();
        System.out.println(response.type);
        Thread.sleep(1000);
    }
}
