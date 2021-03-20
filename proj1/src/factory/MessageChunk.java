package factory;

import main.Definitions;
import main.Peer;

import java.util.Arrays;

public class MessageChunk extends MessageFactory{
    byte[] fileContent;
    String chunkNo;
    public MessageChunk(String fileId,  String chunkNo, byte[] body){
        super(Definitions.CHUNK, fileId);
        this.fileId = fileId;
        this.fileContent = body;
        this.chunkNo = chunkNo;
    }
    public byte[] createMessage() {
        byte[] header = generateHeader();

        // Array concatenation
        byte[] both = Arrays.copyOf(header, header.length + fileContent.length);
        System.arraycopy(fileContent, 0, both, header.length, fileContent.length);

        return both;
    }

    public byte[] generateHeader() {
        // TODO : to fix the version.
        String version = "1.0";
        String header = version + " " + type + " " + Peer.peer_no + " " + fileId + " " +  chunkNo + "\r\n";
        System.out.println("HEADER " + header);
        return header.getBytes();
    }
}
