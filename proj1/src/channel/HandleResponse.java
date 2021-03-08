package channel;

import factory.MessageParser;

public class HandleResponse extends Thread{

    public final MessageParser messageParser;
    public HandleResponse(MessageParser messageParser){
        this.messageParser = messageParser;
    }

    @Override
    public void run(){

    }
}
