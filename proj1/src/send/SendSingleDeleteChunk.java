package send;

import main.Peer;
import main.etc.Singleton;

public class SendSingleDeleteChunk extends Send{

    String destinationId;
    String fileId;
    String chunkNo;
    public SendSingleDeleteChunk(String destinationId, String fileId, String chunkNo) {
        super(Singleton.SINGLEDELETECHUNK, Peer.mc_addr, Peer.mc_port);
        this.fileId = fileId;
        this.chunkNo = chunkNo;
        this.destinationId = destinationId;
    }

    @Override
    protected byte[] buildMessage(MessageBuilder messageBuilder) {
        messageBuilder.addFileId(fileId);
        messageBuilder.addChunkNo(chunkNo);
        messageBuilder.addDestinationId(destinationId);
        return messageBuilder.build();
    }
}
