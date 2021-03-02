import java.io.*;
import java.net.*;
import java.util.*;


public class MultiPeriodThread extends ServerThread{
    private String multiCastAddress; 
    private String multiCastPort; 
    public MultiPeriodThread(int port, String multiCastAddress , String multiCastPort) throws IOException{
        this.multiCastAddress = multiCastAddress; 
        this.multiCastPort = multiCastPort; 
        run(); 
    }
    
    public void run(){
        String message = "teste";
        int TIMEOUT = 5000;
        while(true){
            sendPacket(message);  /*String, DatagramPacket*/ 
            try {
                sleep(TIMEOUT);
            } catch (InterruptedException e) {}
        } 
    }

}
