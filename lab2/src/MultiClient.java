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
        int multiCastPort = Integer.parseInt(args[1]);

        String request = args[2].toUpperCase() + " ";
        for(int i = 3; i < args.length; i++) {
            request += args[i] + " ";
        }

        MulticastSocket multiSocket = new MulticastSocket(4446);
        InetAddress address = InetAddress.getByName("230.0.0.1");

        multiSocket.joinGroup(address);

        // Do it once or more hehe
        String receivePacket = receivePacket(multiSocket); 
        System.out.println(receivePacket); 

        multiSocket.leaveGroup(address);
        multiSocket.close();

        String[] serverInfo = receivePacket.split(" ");
        System.out.println(serverInfo[0]);
        System.out.println(serverInfo[1]);

        String serverIp = serverInfo[0];
        int serverPort = Integer.parseInt(serverInfo[1]);
        // Send Request - Done with RMI
        DatagramSocket socket = new DatagramSocket();

        sendPacket(socket, request, serverIp, serverPort);
        String response = receivePacket(socket);
        
        System.out.println("Client: " + request.trim() + " : " + response);
        socket.close();
    
       
    }

    private static void sendPacket(DatagramSocket socket, String request, String host, int port) throws IOException {
        byte[] buf = request.getBytes();
        InetAddress address = InetAddress.getByName(host); 

        DatagramPacket packet = new DatagramPacket(buf, buf.length, address, port);
        socket.send(packet);
    }

    private static String receivePacket(DatagramSocket socket) throws IOException {
        byte[] buf = new byte[256];
            
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        socket.receive(packet);
 
        return new String(packet.getData(), 0, packet.getLength());
    }
}