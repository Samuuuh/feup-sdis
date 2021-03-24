package process.request;

import main.Definitions;
import main.Peer;
import main.Utils;
import send.SendChunkNo;
import send.SendDelete;
import state.ChunkState;
import state.FileState;

import java.util.concurrent.ConcurrentHashMap;

public class RequestDelete extends Thread {
    protected String fileName;

    public RequestDelete(String fileName){
        this.fileName = fileName;
    }

    @Override
    public void run() {
        try {
            String fileId = Utils.hash(fileName);

            // TODO: Change the place of this
            Peer.peer_state.removeFile(fileId);

            for(int i = 0; i < 5; i++) {
                Thread.sleep(500);
                new SendDelete(Definitions.DELETE, fileId, Peer.mc_addr, Peer.mc_port).start();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
