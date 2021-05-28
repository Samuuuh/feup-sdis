package network.services.reclaim;

import network.Main;
import network.etc.Logger;
import network.message.reclaim.MessageReclaim;

import java.util.concurrent.TimeUnit;

/**
 * After receiving the reclaim message this class will
 * decide what to do with it.
 */
public class HandleRequestReclaim implements Runnable{
    MessageReclaim messageReclaim;
    public HandleRequestReclaim(MessageReclaim messageReclaim){
        this.messageReclaim = messageReclaim;
    }
    @Override
    public void run() {
        // Message still banned.
        if (Main.bannedReclaimMessages.contains(messageReclaim.getMessageId()))
            return;
        Main.bannedReclaimMessages.add(messageReclaim.getMessageId());
        Main.schedulerPool.schedule(new UnbanReclaim(messageReclaim.getMessageId()), 3 * 1000L, TimeUnit.MILLISECONDS);
        Logger.ANY(this.getClass().getName(), "Received reclaim");
    }
}
