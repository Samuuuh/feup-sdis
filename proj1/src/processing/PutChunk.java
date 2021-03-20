package processing;

import factory.MessageParser;
import main.Definitions;
import main.Peer;
import send.SendMessageChunkNo;
import state.ChunkState;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

// TODO: close file after writing.
// SEND TO CONTROL CHANNEL THE MESSAGE
public class PutChunk extends Thread {
    MessageParser messageParsed;
    String saveChunkPath;

    public PutChunk(MessageParser messageParsed) {
        this.messageParsed = messageParsed;
        this.saveChunkPath = Definitions.getFilePath(Peer.peer_no);
    }

    @Override
    public void run() {
        System.out.println("ProcessPutChunk\t:: Treating PUTCHUNK...");

        // Backup file only reads and redirect data
        Boolean fileIsSaved = saveFile(messageParsed);
        addChunkStatus();
        if (fileIsSaved) {
            new SendMessageChunkNo(messageParsed.getVersion(), Definitions.STORED, messageParsed.getFileId(), messageParsed.getChunkNo()).start();
        } else {
            System.out.println("ProcessPutChunk\t:: Error saving file");
        }
    }

    Boolean saveFile(MessageParser messageParsed) {

        System.out.println("ProcessPutChunk\t:: Saving file " + messageParsed.getFileId());

        String filePath = this.saveChunkPath + messageParsed.getFileId() + "-" + messageParsed.getChunkNo();

        try {
            Path path = Paths.get(this.saveChunkPath);
            Files.createDirectories(path);
            File file = new File(filePath);

            if (file.exists()) {
                FileOutputStream outputFile = new FileOutputStream(filePath, true);
                outputFile.write(messageParsed.getData());
                outputFile.close();
            } else {
                file.createNewFile();
            }

            FileOutputStream outputStream = new FileOutputStream(filePath);
            outputStream.write(messageParsed.getData());
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public void addChunkStatus() {
        String id = messageParsed.getFileId() + "-" + messageParsed.getChunkNo();
        int size = messageParsed.getData().length;
        int repDeg = Integer.parseInt(messageParsed.getReplicationDeg());
        ChunkState chunkState = new ChunkState(id, size, repDeg);
        Peer.peer_state.putChunk(id, chunkState);
        System.out.println("PUTCHUNK STATE");
        Peer.peer_state.printState();
    }
}
