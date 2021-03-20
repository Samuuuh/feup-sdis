package state;

public class ChunkStatus implements Status {
    private final String id;
    private final int size;
    private final int perceivedRepDeg;

    public ChunkStatus(String id, int size, int repDeg) {
        this.id = id;
        this.size = size;
        this.perceivedRepDeg = repDeg;
    }

    // FileStatus.
    public ChunkStatus(String id, int repDeg) {
        this.id = id;
        this.size = 0;      // Not necessary information.
        this.perceivedRepDeg = repDeg;
    }

    public String getId() {
        return this.id;
    }

    public int getPerceivedRepDeg() {
        return perceivedRepDeg;
    }

    public int getSize() {
        return size;
    }

    @Override
    public String toString() {
        return "ID: " + id + " SIZE: " + size + " REPDEG: " + perceivedRepDeg;
    }
}
