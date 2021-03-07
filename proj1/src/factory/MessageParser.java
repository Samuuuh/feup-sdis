package factory;

class MessageParser {
    public static void main(String args[]) {  
        String message = "PUTCHUNK";
        Chunk cho = new Chunk(0, 3);
        MessageFactory p = new MessageFactory("fileName");    
        MessageParser mes = new MessageParser(message, p, cho); // Cria
        
        System.out.println(p.getFileId());
        System.out.println(mes.createHeader());


        MessageParser mes1 = new MessageParser("1.0 SenderId ae00a84b73400bc980fa8fbe400342ac9397c3a3f1ae746803ebbc38df3b6e37 0 3");

        System.out.println(mes1.createHeader());
    }  

    // Header
    private String version;
    private String messageType;
    private String senderId;
    private String fileId;
    private String chunkNo;
    private String replicationDeg;

    private String headerString;
    private byte[] header;

    // Body
    private String Data;

    public MessageParser(String header) {
        this.version = "To parse";
        this.messageType = "To parse";
        this.senderId = "To parse";
        this.fileId = "To parse";
        this.chunkNo = "To parse";
        this.replicationDeg = "To parse";
    }

    public MessageParser(String messageType, MessageFactory file, Chunk chunk) {
        this.version = "1.0";
        this.messageType = messageType;
        this.senderId = "SenderId";
        this.fileId = file.getFileId();
        this.chunkNo = String.valueOf(chunk.getChunkNo());
        this.replicationDeg = String.valueOf(chunk.getReplicationDeg());
    }

    public String createHeader() {
        headerString = this.version + " " + this.senderId + " " + this.fileId + " " + this.chunkNo  + " " + this.replicationDeg + "\r\n";
        return headerString;
    }
}