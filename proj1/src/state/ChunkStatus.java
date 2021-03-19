package state;

public class ChunkStatus implements  Status{
    private final int id;
    private final int size;
    private final int perceivedRepDeg;

    ChunkStatus(int id, int size, int repDeg){
        this.id = id;
        this.size = size;
        this.perceivedRepDeg = repDeg;
    }
    ChunkStatus(int id, int repDeg){
        this.id = id;
        this.size = 0;      // Not necessary information.
        this.perceivedRepDeg = repDeg;
    }

    public int getId(){
        return this.id;
    }
    public int getPerceivedRepDeg(){
        return perceivedRepDeg;
    }
    public int getSize(){
        return size;
    }
}
