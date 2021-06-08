package network.etc;

import network.Main;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Singleton {
    public static long m = 4;                   // TODO: change later
    public static int REGISTER_PORT = 9999;
    public static int THREAD_SIZE = 128;
    public static int SCHED_SIZE = 32;           // Thread pool scheduler size.
    public static long STABILIZE_TIME = 5;      // Rate of execution of stabilize in seconds.
    public static long FIX_FINGERS_TIME = 3;    // Rate of execution of fix fingers in seconds.
    public static long CHECK_PRED_TIME = 2;     // Rate of execution of check predecessor in seconds.
    public static long SAVE_PERIOD = 5;         // Rate of saving state in seconds .
    public static String STATE_FILENAME = "state.ser";

    /**
    * Generate ID
    * from https://stackoverflow.com/questions/5531455/how-to-hash-some-string-with-sha256-in-java
    * @param text String to Hash
    */
    public static BigInteger encode(String text)  {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(text.getBytes(StandardCharsets.UTF_8));
            BigInteger id = new BigInteger(1, hash);    // Parse it to big integer.
            return id.mod(BigInteger.valueOf((long) Math.pow(2,m)));           // Get the module

        } catch(Exception e) {
            Logger.ERR("network.etc.Singleton", "Not possible to generate id.");
            e.printStackTrace();
        }
        return BigInteger.ZERO; // Error
    }

    /**
     * Hash a string
     * @param identifier
     * @return
     */
    public static String hash(String identifier) {
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

    /**
    * Generate string with ip and port
    * @param ip Ip of the peer
    * @param port Port of the peer
    */
    public static String getIdUncoded(String ip, int port){
        return ip + ":" + port;
    }

    /**
    * Name of the File to backup
    * @param fileName Filename
    * @return String path where the file will be/is saved
    */
    public static String getBackupFilePath(String fileName){
        return "peers/" + Main.chordNode.getId() + "/backup/"  + fileName + ".ser";
    }

    /**
    * Name of the File to restore
    * @param fileName Filename
    * @return String path where the file will be saved
    */
    public static String getRestoreFilePath(String fileName){
        return "peers/ " +Main.chordNode.getId()+ "/restore/" + fileName;
    }

    /**
    * Get the fileName from the path of the file
    * @param filePath Path of the file
    * @return String the name of the file
    */
    public static String getFileName(String filePath){
        var splitPath = filePath.split("/");
        String fileNameWithExt = splitPath[splitPath.length-1];
        return fileNameWithExt.split("\\.")[0];
    }

    /**
    * Get the extension of one file
    * @param filePath Path of the file
    * @return String the extension of the file
    */
    public static String getFileExtension(String filePath){
        var splitPath = filePath.split("/");
        String fileNameWithExt = splitPath[splitPath.length-1];
        String[] fileNameArray = fileNameWithExt.split("\\.");
        if (fileNameArray.length == 1)
            return "";
        return fileNameArray[fileNameArray.length-1];
    }

    /**
    * Get the path for the saved state of peer
    * @return String The path of the state
    */
    public static String getStatePath(){
        return "peers/" + Main.chordNode.getId() + "/";
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

    /**
     * if nodeId > successorId, then it will a turn in the circle.
     * We need to consider the case above to calculate if an id is between others.
     * @param targetId Id that we want to discover the successor.
     * @param nodeId Id of the current node.
     * @param successorId Successor of the current node.
     * @return return a boolean, true case is between the range, else otherwise.
     */
    public static boolean betweenPredecessor(BigInteger targetId, BigInteger nodeId, BigInteger predecessorId){
        if (nodeId.compareTo(predecessorId) < 0){
            return targetId.compareTo(nodeId) > 0 && targetId.compareTo(predecessorId) <= 0;
        } else{
            return targetId.compareTo(nodeId) > 0 || targetId.compareTo(predecessorId) < 0;
        }
    }
}