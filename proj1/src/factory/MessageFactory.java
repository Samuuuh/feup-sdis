package factory;

import main.Definitions;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.math.BigInteger;

abstract class MessageFactory {
    protected String fileName;
    protected String fileId;
    protected String type;
    protected String senderId;
    protected String filePath;
    protected int repDeg;

    public MessageFactory(String filePath, String type, String senderId, int repDeg) {

        String[] splitFilePath = filePath.split("/");
        fileName = splitFilePath[splitFilePath.length-1];

        this.filePath = filePath;
        this.type = type;
        this.senderId = senderId;
        this.repDeg = repDeg;
        this.fileId = null;
    }

    protected abstract byte[] createMessage() throws IOException;

    protected abstract byte[] generateHeader();

    public String getFileId() {
        if (this.fileId == null) {
            return hash();
        } else return fileId;
    }

    private String hash() {
        // Probably add the last time file was modified and other metadata.
        long seconds = System.currentTimeMillis() / 1000L;
        String identifier = this.fileName + String.valueOf(seconds);

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

            this.fileId = hashedString.toString();
            return this.fileId;
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return "-1";
    }




}
