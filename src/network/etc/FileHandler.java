package network.etc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.nio.file.*;

import network.message.Message;
import network.message.MessageBackup;

import static java.nio.file.Files.readAllBytes;

public class FileHandler {
    public static byte[] readFile(String filePath) throws IOException {
        File file = new File(filePath);

        if (file.length() > Integer.MAX_VALUE)
            throw new IOException("File too large to be read");
        try {
            System.out.println("Aqui ");
            return readAllBytes(file.toPath());

        } catch (Exception e) {
            Logger.INFO("main.service.etc.FileHandler", "File does not exist, skiping...");
        }
        return null;
    }

    public static void saveSerialize(String dir, String fileName, MessageBackup serialize) throws IOException {
        // Create directory if not exists
        Path path = Paths.get(dir);
        Files.createDirectories(path);

        File directory = new File(dir);
        if (!directory.exists()) {
            directory.mkdir();
        }
        
        // SaveFile
        String filePath = dir + fileName;
        FileOutputStream outputFile = new FileOutputStream(filePath, true);
        ObjectOutputStream objectOut = new ObjectOutputStream(outputFile);
        
        objectOut.writeObject(serialize);
        outputFile.close();
    }

    public static void saveFile(String dir, String fileName, byte[] bytesMessage) throws IOException {
        // Create directory if not exists
        Path path = Paths.get(dir);
        Files.createDirectories(path);

        File directory = new File(dir);
        if (!directory.exists()) {
            directory.mkdir();
        }
        
        // SaveFile
        String filePath = dir + fileName;
        FileOutputStream outputFile = new FileOutputStream(filePath, true);
        if (bytesMessage != null) outputFile.write(bytesMessage);
        outputFile.close();
    }
}
