package tasks.backup;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * Contains information of the file being backed up.
 */
public class BackupTrackFile implements Serializable {


    /**
     * Key: Number of the chunk.
     * Value: List with the peers_id keeping that file.
     */
    ConcurrentHashMap<String, ArrayList<String>> chunkRepDeg = new ConcurrentHashMap<>();

    // How many tries were done to backup this file.
    private Integer tries = 0;

    public BackupTrackFile(int numberOfChunks){
        for (int i = 0 ; i < numberOfChunks; i++)
            chunkRepDeg.put(String.valueOf(i), new ArrayList<>());
    }

    public Integer getNumTries(){
        return tries;
    }
    /**
     * Get the current replication degree of a file.
     * Complexity O(n).
     */
    public Integer getActualRepDeg(){
        int repDeg = Integer.MAX_VALUE;
        for (Map.Entry<String, ArrayList<String>> entry : chunkRepDeg.entrySet()) {
            ArrayList<String> peerList = entry.getValue();
            repDeg = Math.min(peerList.size(), repDeg);
        }
        return repDeg;
    }

    /**
     * Updates which peers has stored a certain chunk.
     */
    public void updateChunkRepDeg(String chunkNo, String peerId){
        ArrayList<String> peersStoredChunk = chunkRepDeg.get(chunkNo);

        if (!peersStoredChunk.contains(peerId)){
            chunkRepDeg.remove(chunkNo);
            peersStoredChunk.add(peerId);
            chunkRepDeg.put(chunkNo, peersStoredChunk);
        }
    }

    public void increaseTries(){
        this.tries++;
    }

    public Boolean achievedMaxTries(){
        return this.tries == 5;
    }

    // TODO: Save it on a file after update or initialize.
}
