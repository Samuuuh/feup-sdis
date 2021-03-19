package processing;

import main.Definitions;
import main.Peer;

import java.io.File;

public class Restore extends Thread{
    private final String fileName;

    public Restore(String fileName){
        this.fileName = fileName;
    }

    @Override
    public void run(){
        int counter = 0;
        String filePath = "";
        // Will run until the next chunk is not found.
        while(true) {
            String fileHash = Peer.hash(fileName, counter);
            filePath = "peers/" + Peer.peer_no + "/chunks/" + fileHash;
            File file = new File(filePath);
            System.out.println(filePath); 
            if (!file.exists()) break;
            counter++;
        }
        System.out.println("End restore thread");
    }



}
