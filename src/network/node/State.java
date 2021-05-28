package network.node;


import java.util.concurrent.ConcurrentHashMap;

public class State {
    // Files stored.
    private final ConcurrentHashMap<String, Integer> storedFiles;
    // Files that the peer requested backup.

    public State() {
        storedFiles = new ConcurrentHashMap<>();
    }

    public void addStoredFile(String file, int size) {
        storedFiles.put(file, size);
    }

    public Integer getStoredFile(String file) {
        return storedFiles.get(file);
    }

    public ConcurrentHashMap<String, Integer> getStoredFiles(){
        return storedFiles;
    }

    public void cleanState(){
        storedFiles.clear();
   }

   public void printStoredFiles(){
        storedFiles.forEach((key, value)->{
            System.out.println("-- FILE " + key + " :: SIZE " + value);
        });
   }
}
