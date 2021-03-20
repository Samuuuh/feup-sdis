package processing;

import main.Definitions;
import main.Peer;
import main.Utils;

import java.io.File;

public class Restore extends Thread{
    private final String fileName;
    private final int chunkNo;

    public Restore(String fileName, int chunkNo){
        this.fileName = fileName;
        this.chunkNo = chunkNo ;
    }

    @Override
    public void run(){
        int counter = 0;
        String filePath = "";
        // Will run until the next chunk is not found.
            String fileHash = Utils.hash(fileName);
            filePath = "peers/" + Peer.peer_no + "/chunks/" + fileHash;
            File file = new File(filePath);

            if (file.exists()){
                // TODO: send message;
            }


        System.out.println("End restore thread");
    }



}
