package factory;

class Chunk {
    private int num;                // Number of the chunck.
    private int replicationDeg;
    private byte[] data;

    public Chunk(int num, int replicationDeg, byte[] data) {
        this.num = num;
        this.replicationDeg = replicationDeg;
        this.data = data;
    }

    public int getChunkNo() {
        return num;
    }

    public int getReplicationDeg() {
        return replicationDeg;
    }

    public byte[] getChunkData() {
        return data;
    }
}