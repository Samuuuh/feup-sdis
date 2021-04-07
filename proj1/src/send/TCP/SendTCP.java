package send.TCP;

import send.MessageBuilder;

import java.io.DataOutputStream;
import java.net.*;

public class SendTCP extends Thread{
    protected String type;
    protected String addr;
    protected int port;

    public SendTCP(String type, String addr, int port) {
        this.type = type;
        this.addr = addr;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            // TODO: host: Peer IP
            // TODO: port: Received on message
            Socket socket = new Socket("127.0.0.1", 6666);
            DataOutputStream channelSend = new DataOutputStream(socket.getOutputStream());

            // Sending info
            MessageBuilder messageBuilder = new MessageBuilder(type);
            byte[] message = buildMessage(messageBuilder);

            channelSend.write(message, 0, message.length);
            // Receive response
            socket.close();
        }
        catch(Exception e) {
            System.out.println("ERROR");
        }
    }

    protected byte[] buildMessage(MessageBuilder messageBuilder){
        return messageBuilder.build();
    }
}
