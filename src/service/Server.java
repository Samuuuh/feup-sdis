package service;

import service.message.MessageHello;
import service.server.com.SSLServerConnection;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class Server {

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        // first is the port the second is the ip

        int port = Integer.parseInt(args[0]);
        SSLServerConnection con = new SSLServerConnection(port);
        SSLSocket socket = con.accept();
        MessageHello message = new MessageHello();

        var out = new ObjectOutputStream(socket.getOutputStream());
        var in = new ObjectInputStream(socket.getInputStream());

        out.writeObject(message);
        MessageHello response = (MessageHello) in.readObject();
        System.out.println(response.type);
    }
}
