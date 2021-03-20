package send;

import message.MessageBuilder;
import file.Chunk;
import main.Definitions;
import main.Peer;
import main.Utils;

import java.io.IOException;
import java.net.MulticastSocket;

/**
 * 1) Send request;
 * 2) Handle the request and call Backup handler.
 * 3) Send the response.
 * 4) Handle the response.
 */
public class SendMessageBackup extends SendMessage {
    private String fileId;
    private String repDeg;
    Chunk chunk;

    public SendMessageBackup(String filePath, String fileId, String repDeg, Chunk chunk) {
        super(Peer.version, Definitions.PUTCHUNK, fileId);
        this.fileId = Utils.hash(filePath);
        this.repDeg = repDeg;
        this.chunk = chunk;
    }

    @Override
    public void run() {
        try {
            // TODO: Check sleep
            Thread.sleep((long)(Math.random() * 400));

            System.out.println("Send Message Backup\t:: Sending multicast requests...");
            MulticastSocket socket = new MulticastSocket();

            // Build message
            MessageBuilder messageBuilder = new MessageBuilder(Definitions.PUTCHUNK, fileId);
            messageBuilder.addChunkNo(chunk.getChunkNo());
            messageBuilder.addRepDeg(repDeg);
            byte[] message = messageBuilder.buildWithBody(chunk.getChunkData());

            // Send message
            sendMessage(socket, message, Peer.mdb_addr, Peer.mdb_port);

            System.out.println("Send Message Backup\t:: Message sent!");

        } catch (IOException | InterruptedException e) {

            e.printStackTrace();
        }
    }

}
