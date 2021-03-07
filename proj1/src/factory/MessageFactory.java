package factory;
import main.Definitions;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;


public class MessageFactory {
    public static void main(){

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
        int numSplits = (int) Math.ceil((float) fileSize / Definitions.CHUNCK_MAX_SIZE);    // Includes the last chunck be it zero or not.  
        int lastChunckPos = numSplits-1; 
        int remainSize = fileSize % Definitions.CHUNCK_MAX_SIZE;                            // Size of the last chunck.  
        int bytePos = 0;  

        int emptyChunck = 0; // If an empty empty chunck is necessary, it's value will be 1. 
        if (remainSize == 0)
            emptyChunck = 1;

        byte[][] chuncksContent = new byte[numSplits + emptyChunck][]; 
         
        // Does not compute the last chunck. 
        for (int i = 0; i < lastChunckPos; i++)
            chuncksContent[i] = Arrays.copyOfRange(fileContent, bytePos, bytePos + Definitions.CHUNCK_MAX_SIZE); 

        // Last chunck computation. 
        if (remainSize == 0)
            chuncksContent[lastChunckPos] = new byte[0];  
        else
            chuncksContent[lastChunckPos] = Arrays.copyOfRange(fileContent, bytePos, bytePos + remainSize);

        return chuncksContent;
    }
}
