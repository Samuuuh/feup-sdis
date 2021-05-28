package network.services.reclaim;

import network.Main;
import network.etc.Logger;
import network.message.reclaim.MessageReclaim;

import java.util.concurrent.TimeUnit;

/**
 * After receiving the reclaim message this class will
 * decide what to do with it.
 */
public class Reclaim implements Runnable{
    MessageReclaim messageReclaim;
    public Reclaim(MessageReclaim messageReclaim){
        this.messageReclaim = messageReclaim;
    }
    @Override
    public void run() {
        // Message still banned.
        if (Main.bannedReclaimMessages.contains(messageReclaim.getMessageId())) {
            return;
        }
        else if (Main.chordNode.getId().equals(messageReclaim.getTargetId())){
            Main.bannedReclaimMessages.add(messageReclaim.getMessageId());
            Main.schedulerPool.schedule(new UnbanReclaim(messageReclaim.getMessageId()), 3 * 1000L, TimeUnit.MILLISECONDS);
            reclaim();
        }else{
            Main.bannedReclaimMessages.add(messageReclaim.getMessageId());
            Main.schedulerPool.schedule(new UnbanReclaim(messageReclaim.getMessageId()), 3 * 1000L, TimeUnit.MILLISECONDS);
            Main.threadPool.submit(new RequestReclaim(this.messageReclaim));
        }
    }

    private void reclaim(){
        if (messageReclaim.getSize() == 0){

        }

        Logger.ANY(this.getClass().getName(), "Reclaim done with success.");
    }


}
