package factory;

import main.Definitions;

import java.io.*;
import java.nio.file.Files;
import java.util.Arrays;


public class MessageFactory {
    public static void main() {

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
