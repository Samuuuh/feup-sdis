package network.etc;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Singleton {
    public static int REGISTER_PORT = 9999;
    public static int THREAD_SIZE = 128;
    public static int m = 128;

    public static int getRandomPortNumber(){
        int upperBound = 9000;
        int lowerBound = 7000;
        return (int)Math.floor(Math.random()*(upperBound-lowerBound+1)+lowerBound);
    }

    // from https://stackoverflow.com/questions/5531455/how-to-hash-some-string-with-sha256-in-java
    public static BigInteger encode(String text)  {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            BigInteger id = new BigInteger(1, hash);    // Parse it to big integer.
            return id.mod(BigInteger.valueOf(m));           // Get the module

        }catch(Exception e){
            Logger.ERR("network.etc.Singleton", "Not possible to generate id.");
            e.printStackTrace();

        }
        return BigInteger.ZERO;                             // Error
    }


    public static String getIdUncoded(String ip, int port){
        return ip + ":" + port;
    }
}