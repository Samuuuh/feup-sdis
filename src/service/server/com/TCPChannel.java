package service.server.com;

import service.etc.Logger;

import java.io.*;

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
