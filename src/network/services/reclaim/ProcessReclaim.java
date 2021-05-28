package network.services.reclaim;

import network.Main;

import java.util.concurrent.Callable;

/**
 * This class is responsible for initiating the reclaim.
 */
public class ProcessReclaim implements Callable {

    private int reclaimSize;

    public ProcessReclaim(int reclaimSize){
        this.reclaimSize = reclaimSize;
    }

    @Override
    public Object call()  {
        
        // initiating the reclaim.

        // Delete random files util
        if (reclaimSize == 0){
            System.out.println("reclaim size is zero!");
        }

        System.out.println("stored files ");
        Main.state.printStoredFiles();

        return null;

    }

    public void deleteAllFiles() {
        var filesHash = Main.state.getStoredFiles();
    }
}
