package state;
import java.util.*;

public class FileStatus implements Status{
    private final String pathName;
    private final String fileId;
    private final int desiredRepDeg;
    public List<ChunkStatus> chunkStateList = new ArrayList<ChunkStatus>();

    FileStatus(String pathName, String fileId, int desiredRepDeg){
        this.pathName = pathName;
        this.fileId = fileId;
        this.desiredRepDeg = desiredRepDeg;
    }

    public void addChunk(int chunkId,  int perceivedRepDeg){
        ChunkStatus chunkStatus = new ChunkStatus(chunkId, perceivedRepDeg);
        chunkStateList.add(chunkStatus);
    }

}
