package process.request;

import main.Definitions;
import main.Peer;
import main.Utils;
import send.SendChunkNo;
import state.ChunkState;
import state.FileState;

import java.util.concurrent.ConcurrentHashMap;

public class RequestGetChunk extends Thread{
    protected String fileName;

    public RequestGetChunk(String fileName){
        this.fileName = fileName;
    }


    @Override
    public void run() {
        String fileId = Utils.hash(fileName);

        FileState fileState = Peer.peer_state.getFileState(fileId);

        ConcurrentHashMap<String, ChunkState> chunkHash = fileState.getChunkStateHash();
        System.out.println(chunkHash);

        chunkHash.forEach((chunkId, chunkState) -> {
            String chunkNo = chunkId.split("-")[1];
            new SendChunkNo(Definitions.GETCHUNK, fileId, chunkNo, Peer.mc_addr, Peer.mc_port).start();
        });

    }


};

