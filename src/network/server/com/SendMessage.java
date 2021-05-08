package network.server.com;

import network.etc.Logger;
import network.message.Message;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.net.InetAddress;
import java.io.ObjectOutputStream;

public class SendMessage implements Runnable {
    SSLConnection connectionSocket;

    Message message;

    public SendMessage(String ip, int port, Message message) {
        try {
            System.out.println("Send message -> " + port);
            InetAddress host = InetAddress.getByName("127.0.0.1");
            this.connectionSocket = new SSLConnection(host, port);
            this.message = message;
        }catch(IOException e){
            e.printStackTrace();
            Logger.ERR(this.getClass().getName(), "Not possible to initialize SSLSocket");
        }
    }

    @Override
    public void run() {
        try {
            //System.out.println("Nois é burro");
            //SSLSocket socket = connectionSocket.accept();
            //ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            //out.writeObject(this.message);
            //out.close();
        }
        catch(Exception e) {
            System.out.println("ERROR");
        }
    }

}
