package sendMessage;

import factory.BackupMessageFactory;
import file.Chunk;
import main.Definitions;
import main.Peer;

import java.io.IOException;
import java.net.MulticastSocket;


/**
 * 1) Send request;
 * 2) Handle the request and call Backup handler.
 * 3) Send the response.
 * 4) Handle the response.
 */
public class sendMessageBackup extends sendMessage {
    private String filePath;
    private int replicationDeg;
    Chunk[] chunks;

    public sendMessageBackup(String path, String filePath, String fileId, int replicationDeg, Chunk[] chunks) {
        super(Peer.version, Definitions.PUTCHUNK, fileId);
        this.filePath = filePath;
        this.replicationDeg = replicationDeg;
        this.chunks = chunks;
    }

    @Override
    public void run() {
        try {

            System.out.println("SendMessageBackup\t:: Sending multicast requests...");
            MulticastSocket socket = new MulticastSocket();

            byte[] message =new BackupMessageFactory(filePath, replicationDeg, this.chunks[0]).createMessage();
            sendMessage(socket, message);

            System.out.println("sendMessageBackup\t:: Message sent!");

            //String received = receiveMessage(socket);
            //displayRequest(received);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void displayRequest(String requestMessage){
        System.out.println("Server: " + requestMessage);
    }
}
