package main.etc;

import java.io.File;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Singleton {

    public static final int CHUNK_MAX_SIZE = 64000;
    public static final int REGISTER_PORT = 1886;

    // Type Messages.
    // <Version> PUTCHUNK <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF><CRLF><Body>
    public static String PUTCHUNK = "PUTCHUNK";

    // <Version> STORED <SenderId> <FileId> <ChunkNo> <CRLF><CRLF>
    public static String STORED = "STORED";
    public static String GETCHUNK = "GETCHUNK";
    public static String REMOVED = "REMOVED";

    // <Version> CHUNK <SenderId> <FileId> <ChunkNo> <CRLF><CRLF><Body>
    public static String CHUNK = "CHUNK";

    // <Version> DELETE <SenderId> <FileId> <CRLF><CRLF>
    public static String DELETE = "DELETE";

    // State
    public static String STATE_FILE_NAME = "state.ser";


    public static String getStatePath(String peer_no) {
        return "peers/peer_" + peer_no + "/savedState/";
    }

    // File
    public static String getFilePath(String peer_no) {
        return "peers/peer_" + peer_no + "/chunks/";
    }


    public static String buildChunkId(String fileId, String chunkId){
        return fileId + "-" + chunkId;
    }


    public static String hash(String filePath) {
        File file = new File(filePath);
        String identifier = file.getName() + "/" + file.length() + "/" + file.lastModified();

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(identifier.getBytes(StandardCharsets.UTF_8));

            // Convert byte array into signum representation.
            BigInteger number = new BigInteger(1, hash);

            // Convert message digest into hex value
            StringBuilder hashedString = new StringBuilder(number.toString(16));

            // Pad with leading zeros.
            while (hashedString.length() < 32) {
                hashedString.insert(0, '0');
            }

            return hashedString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "-1";
    }

}
