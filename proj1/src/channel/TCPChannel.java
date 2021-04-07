package channel;

import main.Peer;
import main.etc.Logger;
import main.etc.Singleton;
import process.StoreChunk;
import tasks.restore.RestoreWaiting;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class TCPChannel extends Thread {
    @Override
    public void run() {
        ServerSocket serverSocket = null;
        try {
            // TODO: Send Port on Message?
            // TODO: Send peer IP
            serverSocket = new ServerSocket(6666);
        } catch (IOException e){
            Logger.ERR(this.getClass().getName(),"Error on TCP Channel. Not initializating");
            return;
        }

        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();

                DataInputStream in = new DataInputStream(clientSocket.getInputStream());
                MessageParser messageParsed = new MessageParser(in.readAllBytes());
                in.close();

                if (messageParsed.getMessageType().equals(Singleton.CHUNK)) {
                    // Abort if exists the task to restore the chunk.
                    String chunkId = Singleton.getChunkId(messageParsed.getFileId(), messageParsed.getChunkNo());
                    Peer.restoreTasks.abortTask(chunkId);
                    // Store the chunk locally.
                    if (RestoreWaiting.isWaitingToRestore(chunkId))
                        new StoreChunk(messageParsed).start();
                }

                clientSocket.close();
            } catch (IOException e) {
                Logger.ERR(this.getClass().getName(),"TCP Channel");
            }
        }
    }
}
