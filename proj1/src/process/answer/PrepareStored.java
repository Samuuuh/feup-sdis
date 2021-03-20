package process.answer;

import channel.MessageParser;
import main.Definitions;
import main.Peer;
import send.SendChunkNo;
import state.ChunkState;

import static file.FileHandler.saveFileChunks;

public class PrepareStored extends Thread {
    MessageParser messageParsed;

    public PrepareStored(MessageParser messageParsed) {
        this.messageParsed = messageParsed;
    }

    @Override
    public void run() {
        System.out.println("Preparing stored");
        Boolean fileIsSaved = saveFileChunks(messageParsed);
        addChunkStatus();

        if (fileIsSaved) {
            new SendChunkNo(Definitions.STORED, messageParsed.getFileId(), messageParsed.getChunkNo(), Peer.mc_addr, Peer.mc_port).start();
        } else {
            System.out.println("ProcessPutChunk\t:: Error saving file");
        }
    }


    public void addChunkStatus() {
        String id = messageParsed.getFileId() + "-" + messageParsed.getChunkNo();
        int size = messageParsed.getData().length;
        int repDeg = Integer.parseInt(messageParsed.getReplicationDeg());

        ChunkState chunkState = new ChunkState(id, size, repDeg);
        Peer.peer_state.putChunk(id, chunkState);
        Peer.peer_state.printState();
    }
}
