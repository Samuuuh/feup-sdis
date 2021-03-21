package process.postAnswer;

import channel.MessageParser;
import file.FileHandler;
import main.Peer;

import java.io.IOException;


public class StoreChunk extends Thread {
    private MessageParser messageParsed;
    public StoreChunk(MessageParser messageParsed) {
        this.messageParsed = messageParsed;
    }

    @Override
    public void run() {
        FileHandler.saveFileChunks(messageParsed, "peers/peer_" + Peer.peer_no + "/restore/");

        if (messageParsed.getChunkNo().equals("1")) {
            try {
                FileHandler.saveFile(messageParsed.getFileId(), "peers/peer_" + Peer.peer_no + "/restore/", 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
