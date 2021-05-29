package network.node;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class State {
    // Blocked Delete Messages
    private ConcurrentHashMap<String, String> blockDeleteMessages;
    // Files stored.
    private ConcurrentHashMap<String, String> storedFiles;
    // Files that the peer requested backup.
    private ConcurrentHashMap<String, List<BigInteger>> backedUpFiles;

    public State() {
        blockDeleteMessages = new ConcurrentHashMap<>();
        backedUpFiles = new ConcurrentHashMap<>();
        storedFiles= new ConcurrentHashMap<>();
    }

    // Blocked Messages
    public String getBlockDeleteMessages(String file) {
        return blockDeleteMessages.get(file);
    }

    public void addBlockDeleteMessages(String file) {
        blockDeleteMessages.put(file, file);
    }

    public void removeBlockDeleteMessages(String file) {
        blockDeleteMessages.remove(file);
    }

    // Files Stored
    public String getStoredFile(String file) {
        return storedFiles.get(file);
    }

    public void addStoredFile(String file) {
        storedFiles.put(file, file);
    }

    public void deleteStored(String file) {
        storedFiles.remove(file);
    }
}
