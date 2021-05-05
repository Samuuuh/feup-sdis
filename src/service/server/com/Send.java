package service.server.com;


import etc.Logger;

import javax.net.ssl.SSLSocket;
import java.io.DataOutputStream;
import java.io.IOException;

public class Send extends Thread{
    SSLServerConnection connectionSocket;

    public Send(int port) {
        try {
            this.connectionSocket = new SSLServerConnection(port);
        }catch(IOException e){
            Logger.ERR(this.getClass().getName(), "Not possible to initialize SSLSocket");
        }
    }

    @Override
    public void run() {
        try {
            SSLSocket socket = connectionSocket.accept();
            DataOutputStream out = new DataOutputStream(socket.getOutputStream());
            out.close();
        }
        catch(Exception e) {
            System.out.println("ERROR");
        }
    }

}
