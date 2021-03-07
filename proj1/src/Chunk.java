class Chunk {
    private int num;
    private int replicationDeg;
    private String data;

    public Chunk(int num, int replicationDeg) {
        this.num = num;
        this.replicationDeg = replicationDeg;
        this.data = "None";
    }

    public int getChunkNo() {
        return num;
    }

    public int getReplicationDeg() {
        return replicationDeg;
    }

    public String getChunkData() {
        return data;
    }
}