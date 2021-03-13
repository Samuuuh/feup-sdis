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

    static int splitHeader(byte[] bytes) {
        int i = 0;
        while (true) {
            if (((bytes[i] == (byte) 0x0D) && (bytes[i + 1] == (byte) 0x0A)) || i >= bytes.length) break;
            i++;
        }

        return i + 1;
    }

    static byte[] trim(byte[] bytes) {
        int i = bytes.length - 1;
        while (i >= 0 && bytes[i] == 0) --i;

        return Arrays.copyOf(bytes, i + 1);
    }

    public MessageParser(byte[] byteMessage) {
        try {
            int endHeaderByte = splitHeader(byteMessage);
            byte[] header = Arrays.copyOfRange(byteMessage, 0, endHeaderByte);
            byte[] message = trim(Arrays.copyOfRange(byteMessage, endHeaderByte + 1, byteMessage.length));

            System.out.println(byteMessage.length);
            String messageHeader = new String(header, "ISO-8859-1");

            // Header Parse
            this.headerString = messageHeader.replaceAll("\\s+", " ");
            String[] splitHeader = this.headerString.split(" ");

            this.version = splitHeader[0];
            this.messageType = splitHeader[1];
            this.senderId = splitHeader[2];
            this.fileId = splitHeader[3];


            if (this.messageType.equals(Definitions.PUTCHUNK)) {
                // Empty body.
                parsePutchunk(splitHeader, message);
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

        } catch (Exception e) {
            System.out.println(e);
        }
    }


    void parsePutchunk(String[] splitHeader, byte[] messageByte) throws IOException {
        System.out.println("MessageParser\t:: parsing PUTCHUNK...");

        if (splitHeader.length != 6) {
            System.err.println("Invalid Header");
            throw new IOException();
        }

        this.chunkNo = splitHeader[4];
        this.replicationDeg = splitHeader[5];

        if (messageByte.length == 0) {
            this.data = new byte[0];
        } else {
            this.data = trim(messageByte);

        }

        System.out.println(this.data.length);
        System.out.println("MessageParser\t:: parsed PUTCHUNK!");
    }

    public String getVersion() {
        return version;
    }

    public String getMessageType() {
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