package network.services.delete;

import network.Main;
import network.etc.Logger;

public class RemoveBlockDelete implements Runnable {
    String hash;

    /**
     * Remove Block Delete constructor class. It removes the block of receive
     * a delete message with a given name
     * @param hash Hash of the file
     */
    public RemoveBlockDelete(String hash) {
        this.hash = hash;
    }

    /**
    * Run the remove block delete
    */
    @Override
    public void run() {
        try {
            Logger.INFO(this.getClass().getName(), "Removed Delete Block Message from file with hash " + hash);
            Main.state.removeBlockDeleteMessages(hash);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}