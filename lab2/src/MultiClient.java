import java.io.*;
import java.net.*;
import java.util.*;
 
public class MultiClient {
    public static void main(String[] args) throws IOException {
        if (args.length <= 3) {
             System.out.println("Usage:\n java Client <multicast_addr> <multicast_port> <oper> <opnd>*\n");
             return;
        }

        if ((args[2].equals("LOOKUP")) && (args.length != 4)) {
            System.out.println("Usage:\n java Client <multicast_addr> <multicast_port> <oper> <opnd>*\n");
            return;
        }

        if ((args[2].equals("REGISTER")) && (args.length != 5))  {
            System.out.println("Usage:\n java Client <multicast_addr> <multicast_port> <oper> <opnd>*\n");
            return;
        }

        String multiCastAddr = args[0];
        Integer multiCastPort = Integer.parseInt(args[1]);

        String request = args[2].toUpperCase() + " ";
        for(int i = 3; i < args.length; i++) {
            request += args[i] + " ";
        }

        MulticastSocket socket = new MulticastSocket(multiCastPort);
        InetAddress address = InetAddress.getByName(multiCastAddr);
        socket.joinGroup(address);


        String address = receivePacket(socket); 
        System.out.println(address); 
        /*
        sendPacket(socket, request, multiCastAddr, multiCastPort);
        String response = receivePacket(socket);
        
        System.out.println("Client: " + request.trim() + " : " + response); 
        */
        socket.leaveGroup(address);
        socket.close();
    }


    private static void sendPacket(DatagramSocket socket, String request, String multiCastAddr, Integer multiCastPort) throws IOException {
        byte[] buf = request.getBytes();
        /* InetAddress address = InetAddress.getByName(multiCastAddr); */ 

        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, multiCastPort);
        socket.send(packet);
    }

    private static String receivePacket(DatagramSocket socket) throws IOException {
        byte[] buf = new byte[256];
            
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
 
        return new String(packet.getData(), 0, packet.getLength());
    }
}