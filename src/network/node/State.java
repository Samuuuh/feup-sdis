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
    }

    public Integer getStoredFile(String file) {
        return storedFiles.get(file);
    }

    public ConcurrentHashMap<String, Integer> getStoredFiles(){
        return storedFiles;
    }

    public void cleanState(){
        storedFiles.clear();
        occupiedSize = 0;
   }

   public void printStoredFiles(){
        storedFiles.forEach((key, value)->{
            System.out.println("-- FILE " + key + " :: SIZE " + value);
        });
   }
}
