package state;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChunkState implements Serializable {
    private final String id;
    private final int size;
    private int perceivedRepDeg;
    private int desiredRepDeg;
    private List<String> storedPeers = new ArrayList<>();

    public ChunkState(String id, int size, int desiredRepDeg) {
        this.id = id;
        this.size = size;
        this.desiredRepDeg = desiredRepDeg;
        this.perceivedRepDeg = 0;
    }

    // Used at FileStatus where the size is not necessary.
    public ChunkState(String id, int repDeg) {
        this.id = id;
        this.size = 0;
        this.perceivedRepDeg = repDeg;
    }

    public void addStoredPeer(String peer){
        if (!storedPeers.contains(peer)){
            storedPeers.add(peer);
            perceivedRepDeg = storedPeers.size();
        }
    }

    @Override
    public String toString() {
        return " ID: " + id + " SIZE: " + size + " REPDEG: " + perceivedRepDeg + "\n";
    }
}
