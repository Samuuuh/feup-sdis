package main.etc;

import channel.MessageParser;
import main.Peer;
import tasks.restore.RestoreWaiting;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

import static java.nio.file.Files.readAllBytes;

public class FileHandler {
    public static byte[] readFile(String filePath) throws IOException {
        File file = new File(filePath);

        if (file.length() > Integer.MAX_VALUE)
            throw new IOException("File too large to be read");
        try {
            return readAllBytes(file.toPath());

        } catch (Exception e) {
            Logger.INFO("main.etc.FileHandler", "File does not exist, skiping...");
        }
        return null;
    }

    public static void deleteChunk(String chunkId) throws IOException{
        File file = new File(Singleton.getFilePath(Peer.peer_no) + "/" + chunkId);
        file.delete();
    }

    public static void deleteChunks(String fileId, String filePath) throws IOException {
        File folder = new File(filePath);

        File[] files = folder.listFiles((file, s) -> s.matches(fileId + "-\\d+"));

        for (File file : files) {
            if (!file.delete()) {
                System.err.println( "Can't remove " + file.getAbsolutePath() );
            }
        }
    }

    public static void saveFile(String fileId, String filePath, int chunkNo) throws IOException {
        RestoreWaiting.removeWaitingToRestore(fileId);

        File mainFile = new File(filePath + fileId);
        mainFile.createNewFile();

        FileOutputStream outputFile = new FileOutputStream(filePath + fileId, true);
        for (int i = 0; i < chunkNo; i++) {
            byte[] message = readFile(filePath + fileId + '-' + i);
            if (message != null) outputFile.write(message);
        }

        outputFile.close();
        deleteChunks(fileId, filePath);
    }

    public static Boolean saveFileChunks(MessageParser messageParsed, String dirPath) {
        String filePath = dirPath + messageParsed.getFileId() + "-" + messageParsed.getChunkNo();

        try {
            Path path = Paths.get(dirPath);
            Files.createDirectories(path);
            File file = new File(filePath);

            if (file.exists()) {
                FileOutputStream outputFile = new FileOutputStream(filePath, true);
                outputFile.write(messageParsed.getData());
                outputFile.close();
            } else {
                file.createNewFile();
            }

            FileOutputStream outputStream = new FileOutputStream(filePath);
            outputStream.write(messageParsed.getData());
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

    public static Chunk[] splitFile(byte[] fileContent) {

        if (fileContent.length == 0) {
            Chunk chunk = new Chunk("0", new byte[0]);
            return new Chunk[]{chunk};
        }

        byte[] data;

        // Includes the last chunk be it zero or not.
        int numSplits = (int) Math.ceil((float) fileContent.length / (float) Singleton.CHUNK_MAX_SIZE); // 1
        int lastChunkPos = numSplits - 1;

        int bytePos = 0;

        int remainSize = fileContent.length % Singleton.CHUNK_MAX_SIZE;   // Size of the last chunk.
        int emptyChunk = 0;
        if (remainSize == 0) {
            emptyChunk = 1;
            lastChunkPos ++;
        }

        Chunk[] chunks = new Chunk[numSplits + emptyChunk];

        // Does not compute the last chunk.
        for (int i = 0; i < lastChunkPos; i++) {
            data = Arrays.copyOfRange(fileContent, bytePos, bytePos + Singleton.CHUNK_MAX_SIZE);
            chunks[i] = new Chunk(Integer.toString(i), data);
            bytePos += Singleton.CHUNK_MAX_SIZE;
        }

        // Last chunk computation.
        if (emptyChunk == 1) {
            chunks[lastChunkPos] = new Chunk(Integer.toString(lastChunkPos), new byte[0]);
        }
        else {
            data = Arrays.copyOfRange(fileContent, bytePos, bytePos + remainSize);
            chunks[lastChunkPos] = new Chunk(Integer.toString(lastChunkPos), data);
        }

        return chunks;
    }

    public static Chunk getChunk(byte[] fileContent, int chunkNo){
        int numSplits = (int) Math.ceil((float) fileContent.length / (float) Singleton.CHUNK_MAX_SIZE);
        int lastChunkPos = numSplits -1;
        int bytePos = Singleton.CHUNK_MAX_SIZE*chunkNo;
        if (chunkNo == lastChunkPos){
            int remainSize = fileContent.length % Singleton.CHUNK_MAX_SIZE;
            if (remainSize == 0) return new Chunk(Integer.toString(lastChunkPos), new byte[0]);
            else {
                byte[] data = Arrays.copyOfRange(fileContent, bytePos,bytePos + remainSize);
                return new Chunk(Integer.toString(lastChunkPos), data);
            }
        }

        byte[] data = Arrays.copyOfRange(fileContent, bytePos, bytePos + Singleton.CHUNK_MAX_SIZE);
        return new Chunk(Integer.toString(chunkNo), data);


    }
}
