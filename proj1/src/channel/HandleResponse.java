package channel;

import factory.MessageParser;
import main.Definitions;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class HandleResponse extends Thread{
    public final MessageParser messageParser;

    public HandleResponse(MessageParser messageParser){
        this.messageParser = messageParser;
    }
}
