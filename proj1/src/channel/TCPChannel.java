package channel;

import main.Peer;
import main.etc.Singleton;
import process.StoreChunk;
import tasks.restore.RestoreWaiting;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.DatagramPacket;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPChannel extends Thread {
    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(6666);
        }catch(IOException e){
            System.out.println("Unable to listen to port " + 6666);
        }

        // Server Cycle waiting for message
        while(true) {
            // Receive Request
            MessageParser messageParsed;
            Socket clientSocket = null;
            try {
                clientSocket = serverSocket.accept();
            } catch(IOException e){
                System.out.println("Error");
                continue;
            }

            try {
                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                messageParsed = new MessageParser(in.readAllBytes());
                System.out.println(messageParsed.getMessageType());
                in.close();
            } catch (IOException e) {
                System.out.println("Error");
                continue;
            }

            if (messageParsed.getMessageType().equals(Singleton.CHUNK)) {
                System.out.println(messageParsed.getChunkNo());
                // Abort if exists the task to restore the chunk.
                String chunkId = Singleton.getChunkId(messageParsed.getFileId(), messageParsed.getChunkNo());
                Peer.restoreTasks.abortTask(chunkId);
                // Store the chunk locally.
                if (RestoreWaiting.isWaitingToRestore(chunkId))
                    new StoreChunk(messageParsed).start();
            }

            try {
                clientSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
