package network.node;


import java.util.concurrent.ConcurrentHashMap;

public class State {
    // Files stored.
    private ConcurrentHashMap<String, Integer> storedFiles;
    // Files that the peer requested backup.

    public State() {
        storedFiles = new ConcurrentHashMap<String, int>();
    }

    public void addStoredFile(String file, int size) {
        storedFiles.put(file, size);
    }

    public Integer getStoredFile(String file) {
        return storedFiles.get(file);
    }

}
