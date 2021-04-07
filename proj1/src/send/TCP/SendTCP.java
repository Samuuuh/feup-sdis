package send.TCP;

import send.MessageBuilder;

import java.io.DataOutputStream;
import java.net.*;

public class SendTCP extends Thread{ 
    protected String type;
    protected InetAddress address;
    protected int port;

    public SendTCP(String type, InetAddress address, int port) {
        this.type = type;
        this.address = address;
        this.port = port;
    }

    @Override
    public void run() {
        try {
            Socket socket = new Socket(address, port);
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
