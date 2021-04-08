package send.TCP;

import send.MessageBuilder;
import send.Send;

public class SendGetChunkEnh extends Send {
    String chunkNo;
    String fileId;
    String tcp_port;

    public SendGetChunkEnh(String type, String fileId, String chunkNo, String addr, int port, String tcp_port) {
        super(type, addr, port);
        this.chunkNo = String.valueOf(chunkNo);
        this.fileId = fileId;
        this.tcp_port = tcp_port;
    }

    @Override
    protected byte[] buildMessage(MessageBuilder messageBuilder) {
        messageBuilder.addFileId(fileId);
        messageBuilder.addChunkNo(chunkNo);
        messageBuilder.addTcpPort(tcp_port);
        return messageBuilder.build();

    }
}
