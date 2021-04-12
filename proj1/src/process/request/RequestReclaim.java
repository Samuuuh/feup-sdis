package process.request;

import main.Peer;
import main.etc.FileHandler;
import main.etc.Logger;
import main.etc.Singleton;
import send.SendWithChunkNo;

import java.io.File;
import java.io.IOException;
import java.util.Set;

public class RequestReclaim extends Thread {
    protected int reclaimSpace;

    public RequestReclaim(String space) {
        this.reclaimSpace = Integer.parseInt(space);
    }


    @Override
    public void run() {
        Set<String> chunksId = Peer.peer_state.getChunkKeys();

        if (reclaimWithoutDelete()) return;

        Peer.peer_state.totalSpace = this.reclaimSpace;

        if(this.reclaimSpace == 0) {
            File directory = new File(Singleton.getFilePath(Peer.peer_no));
            File[] files = directory.listFiles();

            for (File file : files) {
                String chunkId = file.getName();
                String chunkNo = Singleton.extractChunkNo(chunkId);
                String fileId = Singleton.extractFileId(chunkId);

                Peer.peer_state.removeChunk(chunkId);
                if (!file.delete())
                    Logger.ERR(this.getClass().getName(), "Not able to remove " + file);
                new SendWithChunkNo(Singleton.REMOVED, fileId, chunkNo, Peer.mc_addr, Peer.mc_port).start();
                Logger.REQUEST(this.getClass().getName(), "REMOVED " + chunkId);
                Peer.peer_state.occupiedSpace = 0;
            }

        }else {

            for (String chunkId : chunksId) {
                if (Peer.peer_state.occupiedSpace <= Peer.peer_state.totalSpace)
                    break;

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
        }
        if (Peer.peer_state.occupiedSpace < 0) Peer.peer_state.occupiedSpace = 0;

        Logger.ANY(this.getClass().getName(), "CURRENT TOTAL SPACE " + Peer.peer_state.totalSpace + " || OCCUPIED SPACE " + Peer.peer_state.occupiedSpace);
    }

    private Boolean reclaimWithoutDelete() {
        if (this.reclaimSpace != 0 && this.reclaimSpace > Peer.peer_state.occupiedSpace) {
            Peer.peer_state.totalSpace = this.reclaimSpace;
            Logger.SUC(this.getClass().getName(), "CURRENT TOTAL SPACE " + Peer.peer_state.totalSpace + " KB || OCCUPIED SPACE " + Peer.peer_state.occupiedSpace + "KB");
            return true;
        }
        return false;
    }
};