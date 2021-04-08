package state;

import main.etc.Singleton;

import java.io.Serializable;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class FileState implements Serializable {
    private final String fileId;
    private final int desiredRepDeg;
    private final String filePath;

    /**
     * Key: ChunkId
     */
    private final ConcurrentHashMap<String, ChunkState> chunkStateHash = new ConcurrentHashMap<>();

    public FileState(String fileId, int desiredRepDeg, String filePath) {
        this.fileId = fileId;
        this.desiredRepDeg = desiredRepDeg;
        this.filePath = filePath;
    }

    public ConcurrentHashMap<String, ChunkState> getChunkStateHash() {
        return chunkStateHash;
    }

    public String getFileId() {
        return fileId;
    }

    public String getFilePath() {
        return filePath;
    }

    public void addChunk(String chunkId, int desiredRepDeg) {
        ChunkState chunkState = new ChunkState(chunkId, desiredRepDeg);
        chunkStateHash.put(chunkId, chunkState);
    }

    public void removeChunkState(String chunkId) {
        chunkStateHash.remove(chunkId);
    }

    public ChunkState getChunkState(String chunkNo) {
        String chunkId = Singleton.getChunkId(fileId, chunkNo);
        return chunkStateHash.get(chunkId);
    }


    public String toString() {

        StringBuilder s = new StringBuilder("\t File Path: " +filePath + "\n");

        s.append("\t FileId: ").append(fileId).append("\n");
        s.append("\t Desired replication degree: ").append(desiredRepDeg).append("\n");
        s.append("\t Chunks: \n");
        Set<String> chunkIds = chunkStateHash.keySet();
        for (String chunkId: chunkIds){
            s.append("\t\t Id: ").append(chunkId).append("\n");
            s.append("\t\tPerceived replication degree: ").append(chunkStateHash.get(chunkId).getPerceivedRepDeg()).append("\n\n");
        }



        return s.toString();
    }
}
