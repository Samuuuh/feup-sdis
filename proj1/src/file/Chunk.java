package file;

public class Chunk {
    private final int chunkNo;                // Number of the chunck.
    private int replicationDeg;
    private final byte[] data;

    public Chunk(int chunkNo, byte[] data){
        this.chunkNo = chunkNo;
        this.replicationDeg = 1;
        this.data = data;
    }

    public Chunk(int chunkNo, int replicationDeg, byte[] data) {
        this.chunkNo = chunkNo;
        this.replicationDeg = replicationDeg;
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

    public void setReplicationDeg(int replicationDeg){
        this.replicationDeg = replicationDeg;
    }
}