package process.request;

import main.Peer;
import main.etc.FileHandler;
import main.etc.Logger;
import main.etc.Singleton;
import send.SendChunkNo;
import state.State;

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
        Set<String> getRandom = Peer.peer_state.getChunkKeys();

        int remainingSpace = state.State.totalSpace - state.State.occupiedSpace;
        if (remainingSpace > this.reclaimSpace) {
            state.State.totalSpace -= this.reclaimSpace;
            Logger.SUC(this.getClass().getName(), "CURRENT TOTAL SPACE " + state.State.totalSpace + " || OCCUPIED SPACE " + state.State.occupiedSpace );
            return;
        }

        // Shrink the part without files.
        state.State.totalSpace = state.State.occupiedSpace;
        System.out.println(state.State.occupiedSpace);
        if (this.reclaimSpace == 0) this.spaceAfterReclaim = 0;
        for(String chunkId: getRandom) {
            if (state.State.totalSpace <= spaceAfterReclaim) {
                if (state.State.totalSpace < 0) state.State.totalSpace = 0;
                state.State.occupiedSpace = state.State.totalSpace;
                break;
            }
            state.State.totalSpace -= Peer.peer_state.getChunkState(chunkId).getSize();
            String[] splitValues = chunkId.split("-");
            String fileId = splitValues[0];
            String chunkNo = splitValues[1];

            try {
                FileHandler.deleteChunk(chunkId);
                Peer.peer_state.removeChunk(chunkId);
            } catch (IOException e) {}

            new SendChunkNo(Singleton.REMOVED, fileId, chunkNo, Peer.mc_addr, Peer.mc_port).start();
            Logger.REQUEST(this.getClass().getName(), "REMOVED " + chunkId);
        }

        Logger.ANY(this.getClass().getName(), "CURRENT TOTAL SPACE " + state.State.totalSpace + " || OCCUPIED SPACE " + state.State.occupiedSpace);


    }
};