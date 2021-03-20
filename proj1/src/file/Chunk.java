package file;

public class Chunk {
    private final int chunkNo;
    private int replicationDeg;
    private final byte[] data;

    public Chunk(int chunkNo, byte[] data) {
        this.chunkNo = chunkNo;
        this.replicationDeg = 1;
        this.data = data;
    }

    public int getChunkNo() {
        return chunkNo;
    }

    public int getReplicationDeg() {
        return replicationDeg;
    }

    public byte[] getChunkData() {
        return data;
    }
}