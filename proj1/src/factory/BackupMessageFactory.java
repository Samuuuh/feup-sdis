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
public class BackupMessageFactory extends MessageFactory {
    Chunk chunk;
    String repDeg;
    String filePath;

    public BackupMessageFactory(String filePath, String repDeg, Chunk chunk) {
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

    protected String hash() {
        // Probabed Strinly add the last time file was modified and other metadata.
        File file = new File(this.filePath);
        String identifier = file.getName() + "/" + file.length() + "/" + file.lastModified();

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(identifier.getBytes(StandardCharsets.UTF_8));

            // Convert byte array into signum representation.
            BigInteger number = new BigInteger(1, hash);

            // Convert message digest into hex value
            StringBuilder hashedString = new StringBuilder(number.toString(16) + "-" + this.chunk.getChunkNo());

            // Pad with leading zeros.
            while (hashedString.length() < 32) {
                hashedString.insert(0, '0');
            }

            this.fileId = hashedString.toString();
            return this.fileId;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "-1";
    }

    @Override
    protected byte[] generateHeader() {
        // TODO : to fix the version. How to store the version of a file?
        // Eh a versao do protocolo do projeto. Tem que ser passado como parametro.
        // TODO: will the replication degree be the same for all the headers for a file?
        // Sim.

        String version = "1.0";
        String header = version + " " + Definitions.PUTCHUNK + " " + Peer.peer_no + " " + hash() + " " + chunk.getChunkNo() + " " + repDeg + "\r\n";
        System.out.println("HEADER " + header);
        return header.getBytes();
    }
}
