import java.io.*;
import java.net.*;
import java.util.*;

public class Server {
    private static DatagramSocket socket = null;
    private static HashMap<String, String> dnsTable = new HashMap<String, String>();

    public Server(int port) throws IOException {
        socket = new DatagramSocket(port);

        run();
    }

    public static void run() {
        byte[] buf = new byte[256];
        DatagramPacket packet = new DatagramPacket(buf, buf.length);
        
        while (true) {
            try {
                String requestMessage = receivePacket(packet);
                displayRequest(requestMessage);
                String response = processRequest(requestMessage);
                sendPacket(response, packet);
                

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private static String processRequest(String messageString) {
        messageString =  messageString.trim();
        final String[] listMessageString = messageString.split("\\s+");

        return switch (listMessageString[0]) {
            case "LOOKUP" -> lookup(listMessageString[1]);
            case "REGISTER" -> register(listMessageString[1], listMessageString[2]);
            default -> "-1";
        };
    }

    // It returns -1, if the name has already been registered, and the number of bindings in the service, otherwise.
    private static String register(String dnsName, String ipAddress) {
        if(dnsTable.get(dnsName) == null) {
            dnsTable.put(dnsName, ipAddress);
            // Return: number of bindings in the service?
            return "OK";    //TODO: No specification.
        } else {
            return "-1";
        }
    }

    // to retrieve the IP address previously bound to a DNS name. 
    // It returns the IP address in the dotted decimal format or the NOT_FOUND string, if the name was not previously registered.
    private static String lookup(String dnsName) {
        if(dnsTable.get(dnsName) == null) {
            return "NOT_FOUND";
        }
        else {
            return dnsTable.get(dnsName);
        }
    }

    private static void sendPacket(String response, DatagramPacket packet) throws IOException {
        InetAddress address = packet.getAddress();
        int port = packet.getPort();

        byte[] buf = response.getBytes();
        packet = new DatagramPacket(buf, buf.length, address, port);
        
        socket.send(packet);
    }

    private static String receivePacket(DatagramPacket packet) throws IOException{
        socket.receive(packet);
        byte[] messageBytes = packet.getData();

        return new String(messageBytes);

    }

    private static void displayRequest(String requestMessage){
        System.out.println("Server: " + requestMessage);
    }
}