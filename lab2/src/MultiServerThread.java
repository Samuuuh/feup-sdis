import java.io.*;
import java.net.*;
import java.util.*;

import javax.print.event.PrintJobListener;

public class MultiServerThread extends ServerThread {
    protected String multicastIp; 
    protected Integer multicastPort; 
    private long TIMEOUT = 1000;

    public MultiServerThread(String[] args) throws IOException{
        super(args);   
        if (args.length < 3){
            System.out.println("Wrong number of arguments:: java Server <srvc_port> <mcast_addr> <mcast_port> "); 
            System.exit(-1); 
        }

        multicastIp = args[1]; 
        multicastPort = Integer.parseInt(args[2]);
        
        System.out.println(args[1]);
    }

    public void run() {
        byte[] buf = new byte[256];

        while (true) {
            try {
                InetAddress group = InetAddress.getByName(multicastIp);
                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, multicastPort); // 4446 -> porta multicaste

                 // send it
                String requestMessage = receivePacket(packet);
                displayRequest(requestMessage);
                String response = processRequest(requestMessage);
                sendPacket(response, packet);

                try {
                    sleep(TIMEOUT);
                } catch (InterruptedException e) { }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}