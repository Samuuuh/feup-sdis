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

        // Concatenate.
        byte[] both = Arrays.copyOf(header, header.length + fileContent.length);
        System.arraycopy(fileContent, 0, both, header.length, fileContent.length);

        return both;
    }


    @Override
    public byte[] generateHeader() {
        // TODO : to fix the version. How to store the version of a file?
        // Eh a versao do protocolo do projeto. Tem que ser passado como parametro.
        // TODO: will the replication degree be the same for all the headers for a file?
        // Sim.

        String version = "1.0";
        String header = version + " " + Definitions.PUTCHUNK + " " + Peer.peer_no + " " + Peer.hash(filePath) + " " + chunk.getChunkNo() + " " + repDeg + "\r\n";
        System.out.println("HEADER " + header);
        return header.getBytes();
    }
}
