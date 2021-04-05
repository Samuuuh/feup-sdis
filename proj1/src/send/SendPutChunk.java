package send;

import main.etc.Chunk;
import main.etc.Singleton;
import main.Peer;


public class SendPutChunk extends Send {
    private final String repDeg;
    Chunk chunk;
    String fileId;
    public SendPutChunk(String fileId, String repDeg, Chunk chunk) {
        super(Singleton.PUTCHUNK, Peer.mdb_addr, Peer.mdb_port);
        this.fileId = fileId;
        this.repDeg = repDeg;
        this.chunk = chunk;
    }

    @Override
    protected byte[] buildMessage(MessageBuilder messageBuilder) {
        messageBuilder.addFileId(fileId);
        messageBuilder.addChunkNo(chunk.getChunkNo());
        messageBuilder.addRepDeg(repDeg);
        return messageBuilder.buildWithBody(chunk.getChunkData());
    }
}
