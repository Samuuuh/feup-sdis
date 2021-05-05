package service.server.com;

import etc.Logger;
import service.message.MessageHello;
import service.server.com.SSLServerConnection;

import javax.net.ssl.SSLSocket;
import java.io.*;
import java.net.InetAddress;

public class TCPChannel extends Thread {
    SSLServerConnection connectionSocket;
    public TCPChannel(int port) {
        try {
            this.connectionSocket = new SSLServerConnection(port);
        }catch(IOException e){
            e.printStackTrace();
            Logger.ERR(this.getClass().getName(), "Not possible to initialize SSLSocket");
        }
    }

    @Override
    public void run() {

    }
}
