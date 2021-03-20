package main;

import java.io.File;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
    public static String hash(String filePath) {
        // Probabed Strinly add the last time file was modified and other metadata.
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
