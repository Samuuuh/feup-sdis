package network.etc;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.nio.file.*;

import network.message.MessageBackup;

import static java.nio.file.Files.readAllBytes;


public class FileHandler {
    public static byte[] readFile(String filePath) throws IOException {
        File file = new File(filePath);

        if (file.length() > Integer.MAX_VALUE)
            Logger.ERR("network.etc.FileHandler","File too large to be read");
        try {
            return readAllBytes(file.toPath());
        } catch (Exception e) {
            Logger.INFO("network.etc.FileHandler", "File does not exist, skiping...");
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

    public static void deleteFile(String dir, String fileName) {
        String filePath = dir + fileName;
        File newFile = new File(filePath);
        if(newFile.delete()) {
            Logger.SUC("network.etc.FileHandler", "File" + fileName + "deleted");
        }
        else {
            Logger.INFO("network.etc.FileHandler", "Not possible delete file " + fileName);
        }
    }

    public static MessageBackup ReadObjectFromFile(String filepath) {
        try {
            FileInputStream fileIn = new FileInputStream(filepath);
            ObjectInputStream objectIn = new ObjectInputStream(fileIn);
            MessageBackup obj = (MessageBackup) objectIn.readObject();
            objectIn.close();
            return obj;
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }
    }

    public static void DeleteFile(String filePath){
        File file = new File(filePath);
        if (!file.delete()) {
            Logger.INFO("network.etc.FileHandler", "Not possible delete file " + filePath);
        }

    }
}
