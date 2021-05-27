package network.node;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class State {
    // Files stored.
    private ConcurrentHashMap<String, String> storedFiles;
    // Files that the peer requested backup.
    private ConcurrentHashMap<String, List<BigInteger>> backedUpFiles;

    public State() {
        backedUpFiles = new ConcurrentHashMap<>();
        storedFiles= new ConcurrentHashMap<>();
    }

    public void addStoredFile(String file) {
        storedFiles.put(file, file);
    }

    public String getStoredFile(String file) {
        return storedFiles.get(file);
    }

}
