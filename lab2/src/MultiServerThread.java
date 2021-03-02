import java.io.*;
import java.net.*;
import java.util.*;

import javax.print.event.PrintJobListener;

public class MultiServerThread extends ServerThread {
    protected String multicastIp; 
    protected Integer multicastPort; 
    private long TIMEOUT = 1000;

    public MultiServerThread(int port, String multicastIp, int multicastPort) throws IOException{
        super(port);   

        this.multicastIp = multicastIp;
        this.multiCastPort = multiCastPort;
    }

    public void run() {
        byte[] buf = new byte[256];

        while (true) {
            try { 
                InetAddress group = InetAddress.getByName(multicastIp);
                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, multicastPort); // 4446 -> porta multicaste
                // TODO: criar thread que enviar o ip do server para o multicast. 
                
                 // send it
                String requestMessage = receivePacket(packet);
                displayRequest(requestMessage);
                String response = processRequest(requestMessage);
                sendPacket(response);

                try {
                    sleep(TIMEOUT);
                } catch (InterruptedException e) { }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}