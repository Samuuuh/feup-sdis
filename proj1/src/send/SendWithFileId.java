package send;

public class SendWithFileId extends Send {
    String fileId;

    public SendWithFileId(String type, String fileId, String addr, int port) {
        super(type, addr, port);
        this.fileId = fileId;

    }

    @Override
    protected byte[] buildMessage(MessageBuilder messageBuilder) {
        messageBuilder.addFileId(fileId);
        return messageBuilder.build();
    }
}
