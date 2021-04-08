package process.request;

import main.Peer;
import main.etc.Logger;
import main.etc.Singleton;
import send.SendWithFileId;
import state.ChunkState;
import state.FileState;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RequestDelete extends Thread {
    protected String fileId;
    public RequestDelete(String filePath){
        this.fileId = Singleton.hash(filePath);
    }

    @Override
    public void run() { 

        peersToDeleteFile();
        try {
            Peer.peer_state.removeFile(fileId);

            for(int i = 0; i < 5; i++) {
                Thread.sleep(500);
                new SendWithFileId(Singleton.DELETE, fileId, Peer.mc_addr, Peer.mc_port).start();
                Logger.REQUEST(this.getClass().getName(), "Requested DELETE on " + fileId + " ATTEMPT No. " + i);
            }
        } catch (InterruptedException e) {
            Logger.ERR(this.getClass().getName(), "Failed REQUESTING DELETE " + fileId);
            e.printStackTrace();
        }
    }

    private void peersToDeleteFile(){
        FileState fileState = Peer.peer_state.getFileState(fileId);
        ConcurrentHashMap<String, ChunkState> chunkHash = fileState.getChunkStateHash();  
        Set<String> peers = new HashSet<>();
        
        chunkHash.forEach((chunkId, ChunkState) ->{ 
            peers.addAll(ChunkState.getStoredPeers());
       });

        peers.forEach((peer_no) ->{
            Peer.peer_state.addFileToDelete(peer_no, fileId);
        });
    }

}
