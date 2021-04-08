package process.request;

import main.Peer;
import main.etc.FileHandler;
import main.etc.Logger;
import main.etc.Singleton;
import send.SendWithChunkNo;
import state.State;

import java.io.IOException;
import java.util.Set;

public class RequestReclaim extends Thread {
    protected int reclaimSpace;

    public RequestReclaim(String space) {
        this.reclaimSpace = Integer.parseInt(space)*1000;
    }


    @Override
    public void run() {
        Set<String> chunksId = Peer.peer_state.getChunkKeys();

        if (reclaimWithoutDelete()) return;

        state.State.totalSpace = this.reclaimSpace;

        for(String chunkId: chunksId) {
            if (state.State.occupiedSpace <= state.State.totalSpace)
                break;

            state.State.occupiedSpace -= Peer.peer_state.getChunkState(chunkId).getSize();
            String fileId = Singleton.extractFileId(chunkId);
            String chunkNo = Singleton.extractChunkNo(chunkId);

            try {
                FileHandler.deleteChunk(chunkId);
                Peer.peer_state.removeChunk(chunkId);
            } catch (IOException e) {
                Logger.ERR(this.getClass().getName(), "Not able to remove " + chunkId);
            }

            new SendWithChunkNo(Singleton.REMOVED, fileId, chunkNo, Peer.mc_addr, Peer.mc_port).start();
            Logger.REQUEST(this.getClass().getName(), "REMOVED " + chunkId);
        }

        if (state.State.occupiedSpace < 0) state.State.occupiedSpace = 0;

        Logger.ANY(this.getClass().getName(), "CURRENT TOTAL SPACE " + state.State.totalSpace + " || OCCUPIED SPACE " + state.State.occupiedSpace);
    }

    private Boolean reclaimWithoutDelete() {
        if (this.reclaimSpace != 0 && this.reclaimSpace > state.State.occupiedSpace) {
            state.State.totalSpace = this.reclaimSpace;
            Logger.SUC(this.getClass().getName(), "CURRENT TOTAL SPACE " + state.State.totalSpace + " KB || OCCUPIED SPACE " + state.State.occupiedSpace + "KB");
            return true;
        }
        return false;
    }
};