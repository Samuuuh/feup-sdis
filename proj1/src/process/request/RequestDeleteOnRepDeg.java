package process.request;

import main.Peer;
import main.etc.Singleton;
import send.SendSingleDelete;
import send.SendSingleDeleteChunk;
import state.ChunkState;
import state.FileState;

public class RequestDeleteOnRepDeg extends Thread {
    private final String fileId;
    private final String chunkId;
    private final String peer_no;

    public RequestDeleteOnRepDeg(String chunkId, String peer_no){
        this.fileId = Singleton.extractFileId(chunkId);
        this.chunkId = chunkId;
        this.peer_no = peer_no;
    }

    @Override
    public void run() {
        FileState fileState = Peer.peer_state.getFileState(fileId);
        String chunkNo = Singleton.extractChunkNo(chunkId);

        if (fileState == null)  return;

        ChunkState chunkState = fileState.getChunkState(chunkNo);

        if (chunkState == null) return;

        if (chunkState.getPerceivedRepDeg() > chunkState.getDesiredRepDeg()) {
            chunkState.removePeer(peer_no);
            new SendSingleDeleteChunk(peer_no, fileId, chunkNo).start();
        }
    }
}
