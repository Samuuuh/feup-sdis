package file;

public class Chunk {
    private final String chunkNo;
    private int replicationDeg;
    private final byte[] data;

    public Chunk(String chunkNo, byte[] data) {
        this.chunkNo = chunkNo;
        this.replicationDeg = 1;
        this.data = data;
    }

    public String getChunkNo() {
        return chunkNo;
    }

    public int getReplicationDeg() {
        return replicationDeg;
    }

    public byte[] getChunkData() {
        return data;
    }
}