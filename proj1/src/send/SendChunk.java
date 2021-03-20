package send;

import main.Peer;


public class SendChunk extends Send {
    String chunkNo;
    byte[] body;


    public SendChunk(String type, String fileId, byte[] body, String chunkNo) {
        super(type, fileId, Peer.mdr_addr, Peer.mdr_port);
        this.body = body;
        this.chunkNo = chunkNo;
    }


    @Override
    protected byte[] buildMessage(MessageBuilder messageBuilder) {
        messageBuilder.addChunkNo(chunkNo);
        return messageBuilder.buildWithBody(body);

    }
}
