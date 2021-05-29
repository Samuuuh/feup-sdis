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

    /**
    * State class constructor
    */
    public State() {
        blockedDeleteMessages = new ConcurrentHashMap<>();
        storedFiles = new ConcurrentHashMap<>();
        blockedReclaimMessages = new ConcurrentHashMap<>();
    }

    /**
    * Add a stored file to the State
    * @param file Name of the file stored
    * @param size int Size of the stored file
    */
    public void addStoredFile(String file, int size) {
        Integer previousSize = storedFiles.get(file);

        // Only stores if the file hasn't been previously stored.
        if (previousSize == null)
            occupiedSize += size;

        storedFiles.put(file, size);

        printStoredFiles();
    }

    /**
    * Get the stored file
    * @param file Name of the file stored
    */
    public Integer getStoredFile(String file) {
        return storedFiles.get(file);
    }

    /**
    * Get Store Files from the State
    * @return ConcurrentHashMap<String, Integer> Concurrent Hash Map with stored files
    */
    public ConcurrentHashMap<String, Integer> getStoredFiles() {
        return storedFiles;
    }

    /**
    * Get State max size
    * @return int maxSize
    */
    public int getMaxSize() {
        return maxSize;
    }

    /**
    * Get the space occupied by the peer
    * @return int occupied size
    */
    public int getOccupiedSize() {
        return occupiedSize;
    }

    /**
    * Set the max size of the peer
    */
    public void setMaxSize(int newMaxSize) {
        this.maxSize = newMaxSize;
    }

    /**
    * Remove the file
    * @param fileName name of the file to be removed
    * @return Integer Size of the file removed
    */
    public Integer removeFile(String fileName) {
        Integer size = storedFiles.remove(fileName);
        if (size != null)
            occupiedSize -= size;
        return size;
    }

    /**
    * Print stored files
    */
    public void printStoredFiles() {
        System.out.println("MAX SIZE: " + this.maxSize);
        System.out.println("OCCUPIED SIZE " + this.occupiedSize);
        storedFiles.forEach((key, value) -> {
            System.out.println("-- FILE " + key + " :: SIZE " + value);
        });
    }


    // BLOCKED DELETE  ----------------------------------------------------
    /**
    * Add the file to the blocked list of Delete Messages
    * @param file File of the delete message
    */
    public void addBlockDeleteMessages(String file) {
        blockedDeleteMessages.put(file, file);
    }

    /**
    * Remove file from the blocked list of Delete Messages
    * @param file File of the delete message
    */
    public void removeBlockDeleteMessages(String file) {
        blockedDeleteMessages.remove(file);
    }

    /**
    * Know if a file is on list of Delete Messages
    * @param file File of the delete message
    * @return String Not null if file in the blocked list
    */
    public String getBlockDeleteMessages(String file) {
        return blockedDeleteMessages.get(file);
    }

    // BLOCKED RECLAIM --------------------------------------------------
    /**
    * Add the id to the blocked list of Reclaim Messages
    * @param id id of the reclaim message
    */
    public void addBlockReclaimMessages(Integer id) {
        blockedReclaimMessages.put(id, id);
    }

    /**
    * Remove the id to the blocked list of Reclaim Messages
    * @param id id of the reclaim message
    */
    public void removeBlockReclaimMessages(Integer id) {
        blockedReclaimMessages.remove(id);
    }

    /**
    * Know if a id is on blocked list of Reclaim Messages
    * @param id id of the reclaim message
    */
    public Integer getBlockReclaimMessages(Integer id) {
        return blockedReclaimMessages.get(id);
    }

    // STATE SAVE ----------------------------------------------------------
    /**
    * Save state of the peer to a file on the filesystem
    */
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
