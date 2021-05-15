package network.etc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import static java.nio.file.Files.readAllBytes;

public class FileHandler {
    public static byte[] readFile(String filePath) throws IOException {
        File file = new File(filePath);

        if (file.length() > Integer.MAX_VALUE)
            throw new IOException("File too large to be read");
        try {
            return readAllBytes(file.toPath());

        } catch (Exception e) {
            Logger.INFO("main.service.etc.FileHandler", "File does not exist, skiping...");
        }
        return null;
    }


    public static void saveFile(String filePath, String fileId, byte[] bytesMessage) throws IOException {
        File mainFile = new File("test-BACKUP.txt");
        mainFile.createNewFile();
        System.out.println("1");

        FileOutputStream outputFile = new FileOutputStream("test-BACKUP.jpg", true);
        System.out.println("2");

        if (bytesMessage != null) outputFile.write(bytesMessage);
        System.out.println("3");
        outputFile.close();
    }
}
