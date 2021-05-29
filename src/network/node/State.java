package network.node;


import network.Main;
import network.etc.Singleton;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ConcurrentHashMap;

public class State implements Serializable {
    // Blocked Delete Messages
    private ConcurrentHashMap<String, String> blockedDeleteMessages;
    private ConcurrentHashMap<Integer, Integer> blockedReclaimMessages;

    // Files stored.
    private final ConcurrentHashMap<String, Integer> storedFiles;
    private int maxSize = 1000000;        // Total size that the program can occupy.
    private int occupiedSize = 0;         // Total size occupied by the program.

    public State() {
        blockedDeleteMessages = new ConcurrentHashMap<>();
        storedFiles = new ConcurrentHashMap<>();
        blockedReclaimMessages = new ConcurrentHashMap<>();
    }

    public void addStoredFile(String file, int size) {
        Integer previousSize = storedFiles.get(file);

        // Only stores if the file hasn't been previously stored.
        if (previousSize == null)
            occupiedSize += size;

        storedFiles.put(file, size);

        printStoredFiles();
    }

    public Integer getStoredFile(String file) {
        return storedFiles.get(file);
    }

    public ConcurrentHashMap<String, Integer> getStoredFiles() {
        return storedFiles;
    }

    public int getMaxSize() {
        return maxSize;
    }

    public int getOccupiedSize() {
        return occupiedSize;
    }

    public void setMaxSize(int newMaxSize) {
        this.maxSize = newMaxSize;
    }


    public Integer removeFile(String fileName) {
        Integer size = storedFiles.remove(fileName);
        if (size != null)
            occupiedSize -= size;
        return size;
    }

    public void printStoredFiles() {
        System.out.println("MAX SIZE: " + this.maxSize);
        System.out.println("OCCUPIED SIZE " + this.occupiedSize);
        storedFiles.forEach((key, value) -> {
            System.out.println("-- FILE " + key + " :: SIZE " + value);
        });
    }


    // BLOCKED DELETE  ----------------------------------------------------

    public void addBlockDeleteMessages(String file) {
        blockedDeleteMessages.put(file, file);
    }

    public void removeBlockDeleteMessages(String file) {
        blockedDeleteMessages.remove(file);
    }

    public String getBlockDeleteMessages(String file) {
        return blockedDeleteMessages.get(file);
    }

    // BLOCKED RECLAIM --------------------------------------------------

    public void addBlockReclaimMessages(Integer id) {
        blockedReclaimMessages.put(id, id);
    }

    public void removeBlockReclaimMessages(Integer id) {
        blockedReclaimMessages.remove(id);
    }

    public Integer getBlockReclaimMessages(Integer id) {
        return blockedReclaimMessages.get(id);
    }

    // STATE SAVE ----------------------------------------------------------

    public void saveState() throws IOException {
        String pathString = Singleton.getStatePath();
        String filePathString = pathString + Singleton.STATE_FILENAME;

        Path path = Paths.get(pathString);
        Files.createDirectories(path);

        FileOutputStream fileOutputStream = new FileOutputStream(filePathString);
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
        objectOutputStream.writeObject(Main.state);
        objectOutputStream.close();
        fileOutputStream.close();
    }

}
