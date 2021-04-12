package state;

import main.etc.Logger;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ChunkState implements Serializable {
    private final String id;
    private final int size;
    private final int desiredRepDeg;
    
    public final List<String> storedPeers = new ArrayList<>();

    public ChunkState(String id, int desiredRepDeg, int size) {
        this.id = id;
        this.size = size;
        this.desiredRepDeg = desiredRepDeg;
        Logger.SUC(this.getClass().getName(), "SAVED IN STATE CHUNK " + id);
    }

    // Used at FileStatus where the size is not necessary.
    public ChunkState(String id, int desiredRepDeg) {
        this.id = id;
        this.size = 0;
        this.desiredRepDeg= desiredRepDeg;
    }

    public int getSize() {
        return size;
    }

    public int getDesiredRepDeg() {
        return desiredRepDeg;
    }

    public List<String> getStoredPeers(){
        return storedPeers;
    }

    public Boolean haveDesiredRepDeg() {
        return storedPeers.size() >= desiredRepDeg;
    }

    public Boolean aboveDesiredRepDeg(){
        return storedPeers.size() > desiredRepDeg;
    }

    public int getPerceivedRepDeg() {
        return storedPeers.size();
    }

    public void addStoredPeer(String peer) {
        if (!storedPeers.contains(peer)) {
            storedPeers.add(peer);
        }
    }

    public void removePeer(String peer) {
        storedPeers.remove(peer);
    }

    public Boolean contains(String peer){
        return storedPeers.contains(peer);
    }

    @Override
    public String toString() {
        StringBuilder s = new StringBuilder("\t ChunkId: " + id + "\n");
        s.append("Size: ").append(size).append(" KBytes\n");
        s.append("Desired replication degree: ").append(desiredRepDeg).append("\n");
        s.append("Perceived replication degree: ").append(getPerceivedRepDeg()).append("\n\n");

        return s.toString();

    }
}
