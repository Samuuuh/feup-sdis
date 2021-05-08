package network.server.com;


import network.etc.Logger;
import network.message.Message;

import javax.net.ssl.SSLSocket;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SendServer implements Runnable {
    SSLServerConnection connectionSocket;
    Message message;

    public SendServer(int port, Message message) {
        try {
            this.connectionSocket = new SSLServerConnection(port);
            this.message = message;
        }catch(IOException e){
            Logger.ERR(this.getClass().getName(), "Not possible to initialize SSLSocket");
        }
    }

    @Override
    public void run() {
        try {
            SSLSocket socket = connectionSocket.accept();
            ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
            out.writeObject(this.message);
            out.close();
        }
        catch(Exception e) {
            System.out.println("ERROR");
        }
    }

}
