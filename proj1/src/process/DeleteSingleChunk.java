package process;

import main.etc.FileHandler;

import java.io.IOException;

public class DeleteSingleChunk extends Thread {

    private final String chunkId;

    public DeleteSingleChunk(String chunkId){
        this.chunkId = chunkId;
    }

    @Override
    public void run(){

        try {
            FileHandler.deleteChunk(chunkId);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
