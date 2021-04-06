package send.TCP;

import main.Peer;
import send.MessageBuilder;

public class SendChunkTCP extends SendTCP {
    String chunkNo;
    byte[] body;
    String fileId;

    public SendChunkTCP(String type, String fileId, byte[] body, String chunkNo) {
        super(type, Peer.mdr_addr, Peer.mdr_port);
        this.fileId = fileId;
        this.body = body;
        this.chunkNo = chunkNo;
    }

    @Override
    protected byte[] buildMessage(MessageBuilder messageBuilder) {
        messageBuilder.addFileId(fileId);
        messageBuilder.addChunkNo(chunkNo);
        return messageBuilder.buildWithBody(body);
    }
}