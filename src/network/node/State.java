package network.node;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class State {
    // Files stored.
    private ConcurrentHashMap<String, String> storedFiles;
    // Files that the peer requested backup.
    private ConcurrentHashMap<String, List<BigInteger>> backedUpFiles;

    public State() {
        backedUpFiles = new ConcurrentHashMap<>();
        storedFiles= new ConcurrentHashMap<>();
    }

    public void addStoredFile(String file) {
        storedFiles.put(file, file);
    }

    public String getStoredFile(String file) {
        return storedFiles.get(file);
    }

    public void addBackedUpFile(String file, BigInteger id){
        List<BigInteger> listPeerIds= backedUpFiles.get(file);
        if (listPeerIds == null) {
            listPeerIds = new ArrayList<>();
        }
        listPeerIds.add(id);
        backedUpFiles.put(file, listPeerIds);
    }

    /**
     * Get a random peer that has stored the file.
     * @param file Hashed filename.
     * @return Case the file hasn't been stored, then it will return a big Integer 0.
     */
    public BigInteger getBackedUpFile(String file){
        List<BigInteger> listPeerIds= backedUpFiles.get(file);
        if (listPeerIds == null) {
            return new BigInteger("0");
        }
        return listPeerIds.get(0);
    }

    public void printBackedUpHash(){
        backedUpFiles.forEach((file, peerIds) ->{
            System.out.println("#file name: " + file);
            for (int i = 0 ; i < peerIds.size(); i++)
                System.out.println("    # peer id: " + peerIds.get(i));
        });
    }
}
