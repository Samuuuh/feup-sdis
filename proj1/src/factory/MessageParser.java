package factory;
import main.Definitions; 

import java.io.UnsupportedEncodingException;

public class MessageParser {/*
    public static void main(String args[]) {  
        String message = "PUTCHUNK";
        Chunk cho = new Chunk(0, 3, new byte[Definitions.CHUNK_MAX_SIZE]);
        MessageFactory p = new MessageFactory("fileName");    
        MessageParser mes = new MessageParser(message, p, cho); // Cria
        
        System.out.println(p.getFileId());
        System.out.println(mes.createHeader());


        MessageParser mes1 = new MessageParser("1.0 SenderId ae00a84b73400bc980fa8fbe400342ac9397c3a3f1ae746803ebbc38df3b6e37 0 3");

        System.out.println(mes1.createHeader());
    }  */

    // Header
    private String version;
    private String messageType;
    private String senderId;
    private String fileId;
    private String chunkNo;
    private String replicationDeg;
    // TODO: put as protected. 
    public String headerString;

    // Body
    private byte[] data;

    public MessageParser(byte[] byteMessage) {
        try {
            String fullMessage = new String(byteMessage, "ISO-8859-1");

            String[] partMessage = fullMessage.split("\r\n"); 

            String messageHeader = partMessage[0];
            this.data = partMessage[1].getBytes();
            
            // Header Parse
            this.headerString = messageHeader.replaceAll("\\s+"," ");
            String[] partHeader = this.headerString.split(" ");

            if(partHeader.length != 6) {
                System.out.println("Invalid Header");
                return;
            }

            this.version = partHeader[0];
            this.messageType = partHeader[1];
            this.senderId = partHeader[2];
            this.fileId = partHeader[3];
            this.chunkNo = partHeader[4];
            this.replicationDeg = partHeader[5];

            for(String elem : partHeader) {
                System.out.println(elem);
            }
        } catch(UnsupportedEncodingException e) {
            System.out.println(e);
        }
    }


    public MessageParser(String header) {
        this.headerString = header.replaceAll("\\s+"," ");
        String[] partHeader = this.headerString.split(" ");

        if(partHeader.length != 6) {
            System.out.println("Invalid Header");
            return;
        }

        this.version = partHeader[0];
        this.messageType = partHeader[1];
        this.senderId = partHeader[2];
        this.fileId = partHeader[3];
        this.chunkNo = partHeader[4];
        this.replicationDeg = partHeader[5];
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