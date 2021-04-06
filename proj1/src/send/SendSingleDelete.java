package send;

public class SendSingleDelete extends Send{

    String destinationId;
    String fileId;
    public SendSingleDelete(String type, String destinationId, String fileId, String addr, int port) {
        super(type, addr, port);
        this.fileId = fileId;
        this.destinationId = destinationId;

    }

    @Override
    protected byte[] buildMessage(MessageBuilder messageBuilder) {
        messageBuilder.addFileId(fileId);
        messageBuilder.addDestinationId(destinationId);
        return messageBuilder.build();
    }
}
