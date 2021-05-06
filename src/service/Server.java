package service;

import service.etc.Logger;
import service.message.MessageBackup;
import service.message.MessageHello;
import service.server.com.SSLServerConnection;
import service.utils.Singleton;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;

// Channel da vida.

/**
 * This class is responsible for listening the client requests and execute them in the network.
 */
public class Server extends Thread {

    int port;

    public Server() throws IOException {
        this.port = 7000;
    }

    @Override
    public void run() {

        SSLServerConnection con = null;

        try {

            con = new SSLServerConnection(port);
            SSLSocket socket = con.accept();
            var out = new ObjectOutputStream(socket.getOutputStream());
            var in = new ObjectInputStream(socket.getInputStream());

            while (true) {
                MessageHello message = new MessageHello("127.0.0.1", port);

                MessageBackup response = (MessageBackup) in.readObject();
                Logger.INFO(this.getClass().getName(), "Sending message");
                out.writeObject(message);
                System.out.println(response.getType());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public int getPort() {
        return port;
    }
}
