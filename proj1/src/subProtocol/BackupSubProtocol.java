package subProtocol;

import factory.BackupMessageFactory;
import file.Chunk;
import java.io.IOException;

public class BackupSubProtocol extends SubProtocol {
    private String filePath;
    private String fileId;
    private String senderId;
    private int replicationDeg;
    Chunk[] chunks;

    public BackupSubProtocol(String filePath, String fileId, String senderId, int replicationDeg, Chunk[] chunks) {
        this.filePath = filePath;
        this.fileId = fileId;
        this.senderId = senderId;
        this.replicationDeg = replicationDeg;
        this.chunks = chunks;
    }

    public void run() {
        try {
            byte[] message = new BackupMessageFactory(filePath, senderId, replicationDeg, this.chunks[0]).createMessage();
            sendPacket(message);
        } catch (IOException var2) {
            var2.printStackTrace();
        }

    }
}
