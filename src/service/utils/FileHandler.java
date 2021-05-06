package service.utils;

import service.etc.Logger;

import java.io.File;
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

}
