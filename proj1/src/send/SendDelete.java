package send;

public class SendDelete extends Send {
    public SendDelete(String type, String fileId, String addr, int port) {
        super(type, fileId, addr, port);
    }

    @Override
    protected byte[] buildMessage(MessageBuilder messageBuilder) {
        return messageBuilder.build();
    }
}
