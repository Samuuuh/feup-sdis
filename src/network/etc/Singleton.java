package network.etc;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class Singleton {
    public static int REGISTER_PORT = 9999;
    public static int THREAD_SIZE = 128;
    public static int SCHED_SIZE = 4;        // Thread pool scheduler size.
    public static long STABILIZE_TIME = 5;   // Rate of execution of stabilize in seconds.
    public static long m = 4;


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
            return id.mod(BigInteger.valueOf((long) Math.pow(2,m)));           // Get the module

        }catch(Exception e){
            Logger.ERR("network.etc.Singleton", "Not possible to generate id.");
            e.printStackTrace();

        }
        return BigInteger.ZERO;                             // Error
    }


    public static String getIdUncoded(String ip, int port){
        return ip + ":" + port;
    }


    /**
     * if nodeId > successorId, then it will a turn in the circle.
     * We need to consider the case above to calculate if an id is between others.
     * @param targetId Id that we want to discover the successor.
     * @param nodeId Id of the current node.
     * @param successorId Successor of the current node.
     * @return return a boolean, true case is between the range, else otherwise.
     */
    public static boolean betweenSuccessor(BigInteger targetId, BigInteger nodeId, BigInteger successorId){
        if (nodeId.compareTo(successorId) < 0){
            return targetId.compareTo(nodeId) > 0 && targetId.compareTo(successorId) < 0;
        } else{
            return targetId.compareTo(nodeId) > 0 || targetId.compareTo(successorId) < 0;
        }
    }

    public static boolean betweenPredecessor(BigInteger targetId, BigInteger nodeId, BigInteger predecessorId){
        if (nodeId.compareTo(predecessorId) < 0){
            return targetId.compareTo(nodeId) > 0 && targetId.compareTo(predecessorId) <= 0;
        } else{
            return targetId.compareTo(nodeId) > 0 || targetId.compareTo(predecessorId) < 0;
        }
    }
}