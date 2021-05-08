package network.server.com;

import network.etc.Logger;
import network.message.Message;

import javax.net.ssl.SSLSocket;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;


/**
 * Server channel. It receives messages from other peers.
 */
public class ChordServer extends Thread {

    int port;
    String ip;

    public ChordServer(String ip, int port) {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public void run() {
        
        SSLServerConnection con = null;

        try {
            con = new SSLServerConnection(port);
            while (true) {
                System.out.println("here");
                SSLSocket socket = con.accept();
                System.out.println("Received");
                var in = new ObjectInputStream(socket.getInputStream());

                Message message = (Message) in.readObject();
                String type = message.getType();

                if (type.equals("backup"))
                    Logger.ANY(this.getClass().getName(), "Received backup message.");
                else if (type.equals("delete"))
                    Logger.ANY(this.getClass().getName(), "Received delete message.");
                else if (type.equals("restore"))
                    Logger.ANY(this.getClass().getName(), "Received restore message");
                else if (type.equals("lookup"))
                    Logger.ANY(this.getClass().getName(), "Received lookup message");
                else
                    Logger.ANY(this.getClass().getName(), "Received " + type + " message");

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    public int getPort() {
        return port;
    }
}
