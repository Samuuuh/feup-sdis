package network.services.delete;

import network.Main;
import network.etc.Logger;

public class RemoveBlockDelete implements Runnable{

    String fileName;

    public RemoveBlockDelete(String fileName){
        this.fileName = fileName;
    }

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