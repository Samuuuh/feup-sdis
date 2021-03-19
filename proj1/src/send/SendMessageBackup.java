package send;

import factory.MessageBackup;
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
public class SendMessageBackup extends SendMessage {
    private String filePath;
    private String replicationDeg;
    Chunk[] chunks;

    public SendMessageBackup(String filePath, String fileId, String replicationDeg, Chunk[] chunks) {
        super(Peer.version, Definitions.PUTCHUNK, fileId);
        this.filePath = filePath;
        this.replicationDeg = replicationDeg;
        this.chunks = chunks;
    }

    @Override
    public void run() {
        try {
            // TODO: Check sleep
            Thread.sleep((long)(Math.random() * 400));

            System.out.println("Send Message Backup\t:: Sending multicast requests...");
            MulticastSocket socket = new MulticastSocket();

            // TODO: For loop on previous file
            for (Chunk chunk : chunks) {
                byte[] message = new MessageBackup(filePath, replicationDeg, chunk).createMessage();
                sendMessage(socket, message, Peer.mdb_addr, Peer.mdb_port);
            }

            System.out.println("Send Message Backup\t:: Message sent!");

        } catch (IOException | InterruptedException e) {

            e.printStackTrace();
        }
    }

}
