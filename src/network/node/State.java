package network.node;

import java.util.concurrent.ConcurrentHashMap;

public class State {
    private ConcurrentHashMap<String, String> backedUpFiles;
    private ConcurrentHashMap<String, String> storedChunks;
}
