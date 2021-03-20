package factory;

import file.Chunk;
import main.Definitions;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import main.Peer;
import main.Utils;

// Creates messages requesting backup.
public class MessageBackup extends MessageFactory {
    Chunk chunk;
    String repDeg;
    String filePath;

    public MessageBackup(String filePath, String repDeg, Chunk chunk) {
        super(Definitions.PUTCHUNK, null);
        this.filePath = filePath;
        this.repDeg = repDeg;
        this.chunk = chunk;
    }

    public byte[] createMessage() throws IOException {
        byte[] header = generateHeader();
        byte[] fileContent = chunk.getChunkData();

        // Array concatenation
        byte[] both = Arrays.copyOf(header, header.length + fileContent.length);
        System.arraycopy(fileContent, 0, both, header.length, fileContent.length);

        return both;
    }

    @Override
    public byte[] generateHeader() {
        // TODO : Fix the version
        String version = "1.0";
        String header = version + " " + Definitions.PUTCHUNK + " " + Peer.peer_no + " " + Utils.hash(filePath) + " " + chunk.getChunkNo() + " " + repDeg + "\r\n";
        System.out.println("HEADER " + header);
        return header.getBytes();
    }
}
