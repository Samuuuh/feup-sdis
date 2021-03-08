package factory;

import main.Definitions;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;  
import java.security.NoSuchAlgorithmException; 
import java.math.BigInteger;  


public class MessageFactory {
    private String fileName;
    private String fileId;

    public MessageFactory(String fileName) {
        this.fileName = fileName;
        this.fileId = null;
    }

    public String getFileId() {
        if(this.fileId == null) {
            return hash();
        }
        else return fileId;
    }

    private String hash() {
        // Probably add the last time file was modified and other metadata
        long seconds = System.currentTimeMillis() / 1000l;
        String identifier = this.fileName + String.valueOf(seconds);

        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(identifier.getBytes(StandardCharsets.UTF_8));

            // Convert byte array into signum representation  
            BigInteger number = new BigInteger(1, hash);  
    
            // Convert message digest into hex value  
            StringBuilder hashedString = new StringBuilder(number.toString(16));  
    
            // Pad with leading zeros 
            while (hashedString.length() < 32) {  
                hashedString.insert(0, '0');  
            }  

            this.fileId = hashedString.toString();
            return this.fileId;
        }
        catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }

        return "-1";
    }

    public static byte[] readFile(String filePath) throws IOException {
        File file = new File(filePath);

        // Check the size.
        if (file.length() > Integer.MAX_VALUE)
            throw new IOException("File too large to be readen");
        try {
            byte[] fileContent = Files.readAllBytes(file.toPath());
            return fileContent;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static byte[][] splitFile(byte[] fileContent) {

        int fileSize = fileContent.length;
        // Includes the last chunk be it zero or not.
        int numSplits = (int) Math.ceil((float) fileSize / Definitions.CHUNK_MAX_SIZE);
        int lastChunkPos = numSplits - 1;
        // Size of the last chunk.
        int remainSize = fileSize % Definitions.CHUNK_MAX_SIZE;
        int bytePos = 0;

        int emptyChunk = 0; // If an empty empty chunk is necessary, it's value will be 1. 
        if (remainSize == 0)
            emptyChunk = 1;

        byte[][] chunksContent = new byte[numSplits + emptyChunk][];

        // Does not compute the last chunk. 
        for (int i = 0; i < lastChunkPos; i++)
            chunksContent[i] = Arrays.copyOfRange(fileContent, bytePos, bytePos + Definitions.CHUNK_MAX_SIZE);

        // Last chunk computation. 
        if (remainSize == 0)
            chunksContent[lastChunkPos] = new byte[0];
        else
            chunksContent[lastChunkPos] = Arrays.copyOfRange(fileContent, bytePos, bytePos + remainSize);

        return chunksContent;
    }
}
