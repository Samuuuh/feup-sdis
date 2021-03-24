package file;

import channel.MessageParser;
import main.Definitions;
import main.Peer;

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
            e.printStackTrace();
        }
        return null;
    }


    public static void deleteChunks(String fileId, String filePath) throws IOException {
        File folder = new File(filePath);

        File[] files = folder.listFiles((file, s) -> s.matches(fileId + "-\\d"));

        for (File file : files) {
            System.out.println(file.getName());

            if (!file.delete()) {
                System.err.println( "Can't remove " + file.getAbsolutePath() );
            }
        }
    }

    public static void saveFile(String fileId, String filePath, int chunkNo) throws IOException {
        File mainFile = new File(filePath + fileId);
        mainFile.createNewFile();

        FileOutputStream outputFile = new FileOutputStream(filePath + fileId, true);
        for (int i = 0; i <= chunkNo; i++) {
            byte[] message = readFile(filePath + fileId + '-' + i);
            if (message != null) outputFile.write(message);
        }

        outputFile.close();

        Peer.removeWaitingToRestore(fileId);
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
        int numSplits = (int) Math.ceil((float) fileContent.length / (float) Definitions.CHUNK_MAX_SIZE);
        int lastChunkPos = numSplits - 1;
        int bytePos = 0;

        int remainSize = fileContent.length % Definitions.CHUNK_MAX_SIZE;   // Size of the last chunk.
        int emptyChunk = 0;
        if (remainSize == 0)
            emptyChunk = 1;

        Chunk[] chunks = new Chunk[numSplits + emptyChunk];

        // Does not compute the last chunk.
        for (int i = 0; i < lastChunkPos; i++) {
            data = Arrays.copyOfRange(fileContent, bytePos, bytePos + Definitions.CHUNK_MAX_SIZE);
            chunks[i] = new Chunk(Integer.toString(i), data);
            bytePos += Definitions.CHUNK_MAX_SIZE;
        }

        // Last chunk computation.
        if (emptyChunk == 1)
            chunks[lastChunkPos] = new Chunk(Integer.toString(lastChunkPos), new byte[0]);
        else {
            data = Arrays.copyOfRange(fileContent, bytePos, bytePos + remainSize);
            chunks[lastChunkPos] = new Chunk(Integer.toString(lastChunkPos), data);
        }

        return chunks;
    }
}
