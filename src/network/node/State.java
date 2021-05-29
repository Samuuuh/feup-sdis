package network.node;


import java.util.concurrent.ConcurrentHashMap;

public class State {

    // Files stored.
    private final ConcurrentHashMap<String, Integer> storedFiles;
    private int maxSize = 1000000;        // Total size that the program can occupy.
    private int occupiedSize = 0;         // Total size occupied by the program.

    public State() {
        storedFiles = new ConcurrentHashMap<>();
    }

    public void addStoredFile(String file, int size) {
        storedFiles.put(file, size);
        occupiedSize += size;
        printStoredFiles();
    }

    public Integer getStoredFile(String file) {
        return storedFiles.get(file);
    }

    public void deleteStored(String file) {
        storedFiles.remove(file);
    }
    public ConcurrentHashMap<String, Integer> getStoredFiles() {
        return storedFiles;
    }

    public int getMaxSize(){
        return maxSize;
    }

    public int getOccupiedSize(){
        return occupiedSize;
    }

    public void setMaxSize(int newMaxSize){
        this.maxSize = newMaxSize;
    }

    public void setOccupiedSize(int occupiedSize){
        this.occupiedSize = occupiedSize;
    }

    public Integer removeFile(String fileName){
        Integer size = storedFiles.remove(fileName);
        if (size != null)
            occupiedSize-=size;
        return size;
    }

    public void printStoredFiles() {
        System.out.println("MAX SIZE: " + this.maxSize);
        System.out.println("OCCUPIED SIZE " + this.occupiedSize);
        storedFiles.forEach((key, value) -> {
            System.out.println("-- FILE " + key + " :: SIZE " + value);
        });
    }
}
