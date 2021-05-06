package service.message;

public class MessageBackup extends Message{

    private byte[] fileBytes;

    public MessageBackup(String ip, int port, byte[] fileBytes) {
        super(ip, port, "backup");
        this.fileBytes = fileBytes;
    }

    public byte[] getFileBytes(){
        return fileBytes;
    }




}
