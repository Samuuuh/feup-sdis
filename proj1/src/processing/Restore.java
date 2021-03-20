package processing;

import file.FileHandler;
import main.Definitions;
import main.Peer;
import main.Utils;
import send.SendMessageRestore;

import java.io.File;


public class Restore extends Thread {
    private final String fileId;
    private final String chunkNo;

    public Restore(String fileId, String chunkNo) {
        this.fileId = fileId;
        this.chunkNo = chunkNo;
    }

    // CHUNK
    @Override
    public void run() {
        try {
            String chunkId = Utils.buildChunkId(fileId, chunkNo);

            String path = Definitions.getFilePath(Peer.peer_no) + chunkId;
            File file = new File(path);
            if (file.exists()) {
                byte[] body = FileHandler.readFile(path);
                new SendMessageRestore(Peer.version, Definitions.CHUNK, fileId, body, chunkNo).start();
            }
        } catch (Exception e) {
            System.out.println("Error Restoring");
        }
    }
}
