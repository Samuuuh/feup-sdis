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
    String repDeg;
    Chunk chunk;
    public MessageBackup(String fileId, Chunk chunk, String repDeg) {
        super(Definitions.PUTCHUNK, fileId);
        this.repDeg = repDeg;
        this.chunk= chunk;
    }

    public byte[] createMessage() {
        byte[] header = createHeader();
        byte[] body = chunk.getChunkData();

        return mergeHeaderBody(header, body);
    }


    @Override
    public byte[] createHeader() {
        // TODO : Fix the version
        String version = "1.0";
        String header = version + " " + type + " " + Peer.peer_no + " " + fileId + " " + chunk.getChunkNo() + " " + repDeg + "\r\n";
        return header.getBytes();
    }
}
