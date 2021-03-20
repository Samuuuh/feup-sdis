package state;

import java.io.Serializable;

public class ChunkState implements Serializable {
    private final String id;
    private final int size;
    private int perceivedRepDeg;

    public ChunkState(String id, int size, int repDeg) {
        this.id = id;
        this.size = size;
        this.perceivedRepDeg = repDeg;
    }

    // Used at FileStatus where the size is not necessary.
    public ChunkState(String id, int repDeg) {
        this.id = id;
        this.size = 0;
        this.perceivedRepDeg = repDeg;
    }

    public void increasePerceivedRepDeg() {
        perceivedRepDeg += 1 ;
    }

    @Override
    public String toString() {
        return " ID: " + id + " SIZE: " + size + " REPDEG: " + perceivedRepDeg + "\n";
    }
}
