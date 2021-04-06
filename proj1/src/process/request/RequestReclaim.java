package process.request;

import main.Peer;
import main.etc.FileHandler;
import main.etc.Logger;
import main.etc.Singleton;
import send.SendWithChunkNo;

import java.io.IOException;
import java.util.Set;

public class RequestReclaim extends Thread {
    protected int reclaimSpace;
    private int spaceAfterReclaim;

    public RequestReclaim(String space) {
        this.reclaimSpace = Integer.parseInt(space)*1000;
        this.spaceAfterReclaim = state.State.totalSpace - this.reclaimSpace;
    }


    @Override
    public void run() {
        Set<String> chunksId = Peer.peer_state.getChunkKeys();

        if (reclaimWithoutDelete()) return;

        // Shrink the part without files.
        state.State.totalSpace = state.State.occupiedSpace;
        if (this.reclaimSpace == 0) this.spaceAfterReclaim = 0;

        for(String chunkId: chunksId) {
            if (state.State.totalSpace <= spaceAfterReclaim)
                break;

            state.State.totalSpace -= Peer.peer_state.getChunkState(chunkId).getSize();
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

        if (state.State.totalSpace < 0) state.State.totalSpace = 0;
        state.State.occupiedSpace = state.State.totalSpace;

        Logger.ANY(this.getClass().getName(), "CURRENT TOTAL SPACE " + state.State.totalSpace + " || OCCUPIED SPACE " + state.State.occupiedSpace);
    }

    private Boolean reclaimWithoutDelete() {
        int currentSpace = state.State.totalSpace - state.State.occupiedSpace;
        if (this.reclaimSpace != 0 && currentSpace > this.reclaimSpace) {
            state.State.totalSpace -= this.reclaimSpace;
            Logger.SUC(this.getClass().getName(), "CURRENT TOTAL SPACE " + state.State.totalSpace + " || OCCUPIED SPACE " + state.State.occupiedSpace );
            return true;
        }
        return false;
    }
};