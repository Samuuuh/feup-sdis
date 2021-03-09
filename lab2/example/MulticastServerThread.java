import java.io.*;
import java.net.*;
import java.util.*;
 
public class MulticastServerThread extends Thread {
    protected DatagramSocket socket = null;
    private long FIVE_SECONDS = 5000;
 
    public MulticastServerThread() throws IOException {
        super("MulticastServerThread");
        socket = new DatagramSocket(4445);
    }
 
    public void run() {
        try {
            while (true) {
                byte[] buf = new byte[256];

                // construct quote
                String dString = "SimpleQuote";
                buf = dString.getBytes();
                // send it
                InetAddress group = InetAddress.getByName("230.0.0.1");
                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, 4446);
                socket.send(packet);
    
                // sleep for a while
            
                sleep((long)(Math.random() * FIVE_SECONDS));
            }
        } catch (Exception e) { 
                e.printStackTrace();
        }
        socket.close();
    }
}
