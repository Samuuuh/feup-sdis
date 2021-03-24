package process.request;

import main.Definitions;
import main.Logger;
import main.Peer;
import main.Utils;
import send.SendChunkNo;
import state.ChunkState;
import state.FileState;

import java.util.concurrent.ConcurrentHashMap;

public class RequestGetChunk extends Thread {
    protected String fileName;

    public RequestGetChunk(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public void run() {
        String fileId = Utils.hash(fileName);

        FileState fileState = Peer.peer_state.getFileState(fileId);
        ConcurrentHashMap<String, ChunkState> chunkHash;

        // Case the file is not in the system.
        try {
            chunkHash = fileState.getChunkStateHash();
        } catch (NullPointerException e) {
            Logger.ERR(this.getClass().getName(), "Not possible to restore file " + fileId);
            return;
        }


        chunkHash.forEach((chunkId, chunkState) -> {
            String chunkNo = chunkId.split("-")[1];
            Peer.addWaitingToRestore(fileId);
            new SendChunkNo(Definitions.GETCHUNK, fileId, chunkNo, Peer.mc_addr, Peer.mc_port).start();
        });

    }


};

