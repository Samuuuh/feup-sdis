import java.io.*;
import java.net.*;
import java.util.*;


public class MultiPeriodThread extends ServerThread {
    private String multiCastAddress; 
    private int multiCastPort; 

    public MultiPeriodThread(int port, String multiCastAddress, int multiCastPort) throws IOException{
        super(port);

        this.multiCastAddress = multiCastAddress; 
        this.multiCastPort = multiCastPort; 
        run(); 
    }
    
    public void run(){
        String message = "teste";
        int TIMEOUT = 5000;

        while(true){
            //sendPacket(message);  /*String, DatagramPacket*/ 
            try {
                sleep(TIMEOUT);
            } catch (InterruptedException e) {}
        } 
    }

}
