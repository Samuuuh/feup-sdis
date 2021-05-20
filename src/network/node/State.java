package network.node;

import java.util.concurrent.ConcurrentHashMap;

public class State {
    private ConcurrentHashMap<String, String> backedUpFiles;
    private ConcurrentHashMap<String, String> storedChunks;

    public State() {

    }

    public void addFile(String file) {
        storedChunks.put(file, file);
    }

    public String getFile(String file) {
        return storedChunks.get(file);
    }
}
