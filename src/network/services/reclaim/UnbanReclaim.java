package network.services.reclaim;

import network.Main;
import network.etc.Logger;

/**
 * This class remove the banned message from the hash.
 * If the peer receives a message with the same id, it will be allowed.
 */
public class UnbanReclaim implements Runnable{

    Integer id;

    public UnbanReclaim(Integer id){
        this.id = id;
    }

    @Override
    public void run() {
        try {
            Main.bannedReclaimMessages.remove(id);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
