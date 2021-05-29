package network.services.delete;

import network.Main;
import network.etc.Logger;

public class RemoveBlockDelete implements Runnable{
    String fileName;

    /**
     * Remove Block Delete constructor class. It removes the block of receive
     * a delete message with a given name
     * @param fileName name of the file to being removed from state 
     */
    public RemoveBlockDelete(String fileName){
        this.fileName = fileName;
    }

    /**
    * Run the remove block delete
    */
    @Override
    public void run() {
        try {
            Logger.INFO(this.getClass().getName(), "Removed Delete Block Message from " + fileName);
            Main.state.removeBlockDeleteMessages(fileName);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}