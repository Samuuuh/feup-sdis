package main.etc;

public class Chunk {
    private final String chunkNo;
    private final byte[] data;

    public Chunk(String chunkNo, byte[] data) {
        this.chunkNo = chunkNo;
        this.data = data;
    }

    public String getChunkNo() {
        return chunkNo;
    }

    public byte[] getChunkData() {
        return data;
    }
}