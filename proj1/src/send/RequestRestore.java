package send;

import main.Definitions;
import main.Peer;
import main.Utils;
import state.ChunkState;
import state.FileState;

import java.util.concurrent.ConcurrentHashMap;

public class RequestRestore extends Thread{
    protected String fileName;

    public RequestRestore(String fileName){
        this.fileName = fileName;
    }


    // GETCHUNK
    @Override
    public void run() {
        String fileId = Utils.hash(fileName);

        FileState fileState = Peer.peer_state.getFileState(fileId);

        ConcurrentHashMap<String, ChunkState> chunkHash = fileState.getChunkStateHash();
        System.out.println(chunkHash);

        chunkHash.forEach((chunkId, chunkState) -> {
            String chunkNo = chunkId.split("-")[1];
            new SendMessageChunkNo(Peer.version, Definitions.GETCHUNK, fileId, chunkNo).start();
        });

    }


};

