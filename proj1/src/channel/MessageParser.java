package channel;

import main.Peer;
import main.etc.Singleton;

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
    private String destinationId;       // Identification of the peer as destination.
    private int tcpPort;

    // Body
    private byte[] data;

    public MessageParser(byte[] byteMessage, int length) {
        try {
            int endHeaderByte = splitHeader(byteMessage);
            byte[] header = Arrays.copyOfRange(byteMessage, 0, endHeaderByte);
            byte[] message = Arrays.copyOfRange(byteMessage, endHeaderByte + 1, length);
            String messageHeader = new String(header, "ISO-8859-1");

            // Header Parser
            this.headerString = messageHeader.replaceAll("\\s+", " ");
            String[] splitHeader = this.headerString.split(" ");
            this.version = splitHeader[0];
            this.messageType = splitHeader[1];
            this.senderId = splitHeader[2];


            if (this.messageType.equals(Singleton.PUTCHUNK)) {
                parsePutchunk(splitHeader, message);
            } else if (this.messageType.equals(Singleton.GETCHUNK) && Peer.version.equals(Singleton.VERSION_ENH)) {
                parseTcpPort(byteMessage);
            } else if (this.messageType.equals(Singleton.STORED) ||
                    this.messageType.equals(Singleton.REMOVED) ||
                    this.messageType.equals(Singleton.GETCHUNK)) {
                parseWithChunkNo(splitHeader);
            } else if (this.messageType.equals(Singleton.CHUNK)) {
                parseChunk(splitHeader, message);
            } else if (this.messageType.equals(Singleton.DELETE) || this.messageType.equals(Singleton.RCVDELETE)) {
                this.fileId = splitHeader[3];
            } else if (this.messageType.equals(Singleton.SINGLEDELETEFILE)) {
                parseSingleDelete(splitHeader);
            } else if (this.messageType.equals(Singleton.SINGLEDELETECHUNK)) {
                parseSingleDeleteChunk(splitHeader);
            }


        } catch (
                Exception e) {
            System.out.println(e);
        }

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

    public int getTcpPort() {
        return tcpPort;
    }

    public String getDestinationId() {
        return destinationId;
    }

    static int splitHeader(byte[] bytes) {
        int i = 0;
        while (true) {
            if (((bytes[i] == (byte) 0x0D) && (bytes[i + 1] == (byte) 0x0A) && (bytes[i + 2] == (byte) 0x0D) && (bytes[i + 3] == (byte) 0x0A)) || i >= bytes.length)
                break;
            i++;
        }
        return i + 3;
    }

    static byte[] trim(byte[] bytes) {
        int i = bytes.length - 1;
        while (i >= 0 && bytes[i] == 0) --i;

        return Arrays.copyOf(bytes, i + 1);
    }

    void parsePutchunk(String[] splitHeader, byte[] messageByte) throws IOException {
        this.fileId = splitHeader[3];
        if (splitHeader.length != 6) {
            System.err.println("Invalid Header");
            throw new IOException();
        }

        this.chunkNo = splitHeader[4];
        this.replicationDeg = splitHeader[5];

        if (messageByte.length == 0) {
            this.data = new byte[0];
        } else {
            this.data = messageByte;
        }
    }

    void parseWithChunkNo(String[] splitHeader) {
        this.fileId = splitHeader[3];
        this.chunkNo = splitHeader[4];
    }

    void parseChunk(String[] splitHeader, byte[] messageParsed) {
        this.fileId = splitHeader[3];
        this.chunkNo = splitHeader[4];
        if (messageParsed.length == 0) {
            this.data = new byte[0];
        } else {
            this.data = messageParsed;
        }
    }

    void parseSingleDelete(String[] splitHeader) {
        this.fileId = splitHeader[3];
        this.destinationId = splitHeader[4];
    }

    void parseSingleDeleteChunk(String[] splitHeader) {
        this.fileId = splitHeader[3];
        this.chunkNo = splitHeader[4];
        this.destinationId = splitHeader[5];
    }

    void parseTcpPort(byte[] header) throws UnsupportedEncodingException {
        String messageHeader = new String(header, "ISO-8859-1");
        String[] splitHeader = messageHeader.split("\r\n");
        parseWithChunkNo(splitHeader[0].split(" "));
        tcpPort = Integer.parseInt(splitHeader[1]);
    }
}