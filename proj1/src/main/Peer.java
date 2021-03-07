package main;

import channel.BackupChannel; 
import main.Definitions; 
import java.io.*;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;


public class Peer implements Services {

    public static int mcast_port; 
    public static String mcast_addr;
    public static int port ;
    
    public static void initChannel(int mcast_port, String mcast_addr) throws IOException {
        new BackupChannel(mcast_port, mcast_addr).start();
    }

    public static void main(String[] args) throws IOException {

        if (args.length != 3) {
            System.out.println("Usage:\n java Peer mcast_port mcast_addr port");
            return;
        }  

        mcast_port = Integer.parseInt(args[0]); 
        mcast_addr = args[1];
        port = Integer.parseInt(args[2]);

        initChannel(mcast_port, mcast_addr);  


        // Bind Services. 
        try {
            Peer obj = new Peer();
            Services stub = (Services) UnicastRemoteObject.exportObject(obj, 0);
            Registry registry = LocateRegistry.createRegistry(1888);
            registry.rebind("Services", stub);    

            System.out.println("Server is running");  
        } catch (Exception e) {
            System.out.println("ERROR: Error while trying to bind stub"); 
            e.printStackTrace();
        }

    }

    public String backup() throws IOException {
        System.out.println("Backup called");
        String message = "VERSION TYPE ID FILE CHUNKNO REPDEG \r\n buguezinha";
        sendPacket(message); 
        
        return "Hello world";
    }

    public static void sendPacket(String response) throws IOException {
        MulticastSocket socket = new MulticastSocket(mcast_port);
        InetAddress group = InetAddress.getByName(mcast_addr);
        byte[] buf = new byte[Definitions.CHUNK_MAX_SIZE];
        DatagramPacket packet = new DatagramPacket(buf, buf.length, group, mcast_port);

        socket.send(packet);
    }
}