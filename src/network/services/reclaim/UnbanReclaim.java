package network.services.reclaim;

import network.Main;

/**
 * This class remove the banned message from the hash.
 * If the peer receives a message with the same id, it will be allowed.
 */
public class UnbanReclaim implements Runnable { 
    Integer id;

    /**
     * Remove reclaim from state
     * @param id peer id
     */
    public UnbanReclaim(Integer id){
        this.id = id;
    }

    /**
     * Run the Unban reclaim
     */
    @Override
    public void run() {
        try {
            Main.state.removeBlockReclaimMessages(id);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
