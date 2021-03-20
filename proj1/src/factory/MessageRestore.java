package factory;

import main.Definitions;
import main.Peer;

import java.io.IOException;
import java.util.Arrays;

public class MessageRestore {
    String fileId;
    byte[] fileContent;
    String chunkNo;
    public MessageRestore(String fileId, byte[] body, String chunkNo){
        this.fileId = fileId;
        this.fileContent = body;
        this.chunkNo = chunkNo;
    }
    public byte[] createMessage() throws IOException {
        byte[] header = generateHeader();

        // Array concatenation
        byte[] both = Arrays.copyOf(header, header.length + fileContent.length);
        System.arraycopy(fileContent, 0, both, header.length, fileContent.length);

        return both;
    }

    public byte[] generateHeader() {
        // TODO : to fix the version.
        String version = "1.0";
        String header = version + " " + Definitions.CHUNK + " " + Peer.peer_no + " " + fileId + " " +  chunkNo + "\r\n";
        System.out.println("HEADER " + header);
        return header.getBytes();
    }
}
