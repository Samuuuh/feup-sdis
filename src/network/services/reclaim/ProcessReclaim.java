package network.services.reclaim;

import network.Main;
import network.message.reclaim.MessageReclaim;

import java.util.concurrent.Callable;

/**
 * This class is responsible for initiating the reclaim.
 */
public class ProcessReclaim implements Callable {

    MessageReclaim messageReclaim;

    public ProcessReclaim(MessageReclaim messageReclaim){
        this.messageReclaim = messageReclaim;
    }

    @Override
    public Object call()  {
        // initiating the reclaim.

        // Delete random files util
        /*if (reclaimSize == 0){
            System.out.println("reclaim size is zero!");
        }*/

        System.out.println("stored files ");
        Main.state.printStoredFiles();

        return null;

    }

    public void deleteAllFiles() {
        var filesHash = Main.state.getStoredFiles();
    }
}
