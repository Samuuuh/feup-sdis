package service.message;

public class MessageHello extends Message{

    public String type;
    public MessageHello (String ip, int port){
        super(ip, port, "hello");
        this.type = "hii";
    }
}
