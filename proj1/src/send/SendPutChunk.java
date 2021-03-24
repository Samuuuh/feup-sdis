package send;

import dataStructure.Chunk;
import main.etc.Singleton;
import main.Peer;


public class SendPutChunk extends Send {
    private final String repDeg;
    Chunk chunk;

    public SendPutChunk(String fileId, String repDeg, Chunk chunk) {
        super(Singleton.PUTCHUNK, fileId, Peer.mdb_addr, Peer.mdb_port);
        this.repDeg = repDeg;
        this.chunk = chunk;
    }

    @Override
    protected byte[] buildMessage(MessageBuilder messageBuilder) {
        messageBuilder.addChunkNo(chunk.getChunkNo());
        messageBuilder.addRepDeg(repDeg);
        return messageBuilder.buildWithBody(chunk.getChunkData());
    }
}
