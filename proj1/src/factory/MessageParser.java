package factory;

import file.Chunk;
import main.Definitions;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;


// <Version> PUTCHUNK <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF><CRLF><Body>
// <Version> STORED <SenderId> <FileId> <ChunkNo> <CRLF><CRLF>
// <Version> GETCHUNK <SenderId> <FileId> <ChunkNo> <CRLF><CRLF>
// <Version> CHUNK <SenderId> <FileId> <ChunkNo> <CRLF><CRLF><Body>
// <Version> DELETE <SenderId> <FileId> <CRLF><CRLF>

public class MessageParser {

    // Header
    private String version;
    private String messageType;
    private String senderId;
    private String fileId;
    private String chunkNo;
    private String replicationDeg;
    private String headerString;
    // Body
    private byte[] data;

    static byte[] splitHeader(byte[] bytes){
        int i = bytes.length - 1;
        while (true) {
            if ((bytes[i-1]  == (byte) 0x0D) && (bytes[i] == (byte) 0x0A)) break;
             --i;
        }

        return Arrays.copyOf(bytes, i + 2);
    }

    static byte[] trim(byte[] bytes) {
        int i = bytes.length - 1;
        while (i >= 0 && bytes[i] == 0) --i;

        return Arrays.copyOf(bytes, i + 1);
    }

    public MessageParser(byte[] byteMessage) {
        try {
            byte[] fileByte = splitHeader(byteMessage);
            try {
                FileOutputStream outputStream = new FileOutputStream("teste3.jpg");
                outputStream.write(fileByte);
            } catch (Exception e) {
                System.out.println("e");
            }

            String fullMessage = new String(byteMessage, "ISO-8859-1");

            String[] splitMessage = fullMessage.split("\r\n");
            String messageHeader = splitMessage[0];

            try {
                FileOutputStream outputStream = new FileOutputStream("teste4.jpg");
                outputStream.write(splitMessage[1].getBytes());
            } catch (Exception e) {
                System.out.println("e");
            }

            // Header Parse
            this.headerString = messageHeader.replaceAll("\\s+", " ");
            String[] splitHeader = this.headerString.split(" ");

            // Check the min side for the
            if (splitHeader.length < 4) {
                System.err.println("MessageParser\t::Invalid Header");
                throw new IOException();
            }


            this.version = splitHeader[0];
            this.messageType = splitHeader[1];
            this.senderId = splitHeader[2];
            this.fileId = splitHeader[3];

            if (this.messageType.equals(Definitions.PUTCHUNK)) {
                parsePutchunk(splitHeader, splitMessage);
            } else if (this.messageType.equals(Definitions.STORED) ||
                    this.messageType.equals(Definitions.REMOVED) ||
                    this.messageType.equals(Definitions.GETCHUNK)) {
                System.out.println("MessageParser\t::To implement");
            } else if (this.messageType.equals(Definitions.CHUNK)) {
                System.out.println("MessageParser\t::To implement");
            } else if (this.messageType.equals(Definitions.DELETE)) {
                System.out.println("MessageParser\t::To implement");
            } else {
                System.out.println("MessageParser\t::Not a valid header");
            }



        } catch (Exception e ) {
            System.out.println(e);
        }
    }



    void parsePutchunk(String[] splitHeader, String[] splitMessage) throws IOException {

        System.out.println("MessageParser\t:: parsing PUTCHUNK...");

        if (splitHeader.length != 6) {
            System.err.println("Invalid Header");
            throw new IOException();
        }

        this.chunkNo = splitHeader[4];
        this.replicationDeg = splitHeader[5];

        // Empty body.
        if (splitMessage.length != 2) {
            this.data = new byte[0];
            System.out.println("MessageParser\t:: parsed PUTCHUNK!");
            return;
        }
        this.data = trim(splitMessage[1].getBytes());

        System.out.println(this.data.length);
        System.out.println("MessageParser\t:: parsed PUTCHUNK!");

    }



    public String getVersion() {
        return version;
    }

    public String getMessageType(){
        return messageType;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getFileId() {
        return fileId;
    }

    public String getChunkNo() {
        return chunkNo;
    }

    public String getReplicationDeg() {
        return replicationDeg;
    }

    public byte[] getData() {
        return data;
    }

/*
    public MessageParser(String header) {
        this.headerString = header.replaceAll("\\s+"," ");
        String[] splitHeader = this.headerString.split(" ");

        if(splitHeader.length != 6) {
            System.out.println("Invalid Header");
            return;
        }

        this.version = splitHeader[0];
        this.messageType = splitHeader[1];
        this.senderId = splitHeader[2];
        this.fileId = splitHeader[3];
        this.chunkNo = splitHeader[4];
        this.replicationDeg = splitHeader[5];
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
    }*/
}