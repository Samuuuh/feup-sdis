import java.io.*;
import java.net.*;
import java.util.*;

public class MultiServer {
    public static void main(String[] args) throws java.io.IOException {
        if (args.length < 3){
            System.out.println("Wrong number of arguments:: java Server <srvc_port> <mcast_addr> <mcast_port> "); 
            System.exit(-1); 
        }
        
        int port = Integer.parseInt(args[0]);
        String multicastIp = args[1]; 
        int multicastPort = Integer.parseInt(args[2]);

        new MultiServerThread(port, multicastIp, multicastPort).start();
        new MultiPeriodThread(port, multicastIp, multicastPort).start(); 
    }
}