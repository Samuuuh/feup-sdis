package process.request;

import tasks.restore.RestoreWaiting;
import main.Peer;
import main.etc.Logger;
import main.etc.Singleton;
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
        String fileId = Singleton.hash(fileName);

        FileState fileState = Peer.peer_state.getFileState(fileId);
        ConcurrentHashMap<String, ChunkState> chunkHash;

        // Case the file is not in the system.
        try {
            chunkHash = fileState.getChunkStateHash();
        } catch (NullPointerException e) {
            Logger.ERR(this.getClass().getName(), "Failed REQUESTING GETCHUNK on " + fileId);
            return;
        }

            chunkHash.forEach((chunkId, chunkState) -> {
            String chunkNo = chunkId.split("-")[1];
            RestoreWaiting.addWaitingToRestore(fileId);
            new SendChunkNo(Singleton.GETCHUNK, fileId, chunkNo, Peer.mc_addr, Peer.mc_port).start();
        });
        Logger.REQUEST(this.getClass().getName(), "Requested GETCHUNK on " + fileId);
    }


};

