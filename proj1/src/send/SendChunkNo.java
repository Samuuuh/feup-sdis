package send;

public class SendChunkNo extends Send {
    String chunkNo;
    public SendChunkNo(String type, String fileId, String chunkNo, String addr, int port) {
        super(type, fileId, addr, port);
        this.chunkNo = String.valueOf(chunkNo);
    }

    @Override
    protected byte[] buildMessage(MessageBuilder messageBuilder) {
        messageBuilder.addChunkNo(chunkNo);
        return messageBuilder.build();
    }
}
