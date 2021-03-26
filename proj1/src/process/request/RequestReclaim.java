package process.request;

import main.Peer;
import main.etc.Logger;
import main.etc.Singleton;
import send.SendChunkNo;
import state.ChunkState;
import state.FileState;

import java.util.concurrent.ConcurrentHashMap;

public class RequestReclaim extends Thread {
    protected String space;

    public RequestReclaim(String space) {
        this.space = space;
    }

    @Override
    public void run() {
        //String fileId = Singleton.hash(fileName);

        //new SendChunkNo(Singleton.GETCHUNK, fileId, chunkNo, Peer.mc_addr, Peer.mc_port).start();

        //Logger.REQUEST(this.getClass().getName(), "Requested GETCHUNK on " + fileId);
    }
};