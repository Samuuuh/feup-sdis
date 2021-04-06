package process.request;

import main.Peer;
import main.etc.Logger;
import main.etc.Singleton;
import send.SendWithChunkNo;
import send.SendWithChunkNoEnhance;
import state.ChunkState;
import state.FileState;
import tasks.restore.RestoreWaiting;

import java.util.concurrent.ConcurrentHashMap;

public class RequestGetChunkEnhance extends Thread {
    protected String fileId;

    public RequestGetChunkEnhance(String filePath) {
        this.fileId = Singleton.hash(filePath);
    }

    @Override
    public void run() {
        FileState fileState = Peer.peer_state.getFileState(fileId);
        ConcurrentHashMap<String, ChunkState> chunksState;

        // Case the file is not in the system.
        try {
            chunksState = fileState.getChunkStateHash();
        } catch (NullPointerException e) {
            Logger.ERR(this.getClass().getName(), "Failed REQUESTING GETCHUNK on " + fileId);
            return;
        }

        addToWaitingList(chunksState);

        Logger.REQUEST(this.getClass().getName(), "Requested GETCHUNK on " + fileId);
    }

    private void addToWaitingList(ConcurrentHashMap<String, ChunkState> chunksState) {
        chunksState.forEach((chunkId,chunkState)->{
            String chunkNo = Singleton.extractChunkNo(chunkId);
            RestoreWaiting.addWaitingToRestore(chunkId);
            new SendWithChunkNoEnhance(Singleton.GETCHUNK, fileId, chunkNo, Peer.mc_addr, Peer.mc_port).start();
        });
    }

};

