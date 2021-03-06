import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;  
import java.security.NoSuchAlgorithmException; 
import java.math.BigInteger;  

class Parser {
    public static void main(String args[]) 
    {  
        Parser p = new Parser("fileName");

        System.out.println(p.getFileId());
    }  

    private String fileName;
    private String fileId;

    public Parser(String fileName) {
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
}
 
 