package network.services.delete;


import network.Main;
import network.etc.Logger;
import network.message.MessageDelete;

public class ProcessDelete implements Runnable {
    MessageDelete message;
    String hash;
    int port;

    /**
     * Process delete
     * @param message the message that was received 
     * @param port the port of delete message 
     */
    public ProcessDelete(MessageDelete message, int port) {
        this.message = message;
        this.hash = message.getHash();
        this.port = port;
    }

    /**
     * Run the process delete protocol
     */
    @Override
    public void run() {
        try {
            if (Main.state.getBlockDeleteMessages(hash) == null) {
                Logger.ANY(this.getClass().getName(), "Received DELETE.");

                Main.threadPool.execute(new SendDelete(hash, message.getOriginNode()));
            }
        } catch (Exception e) {
            Logger.ERR(this.getClass().getName(), "Not possible to send restore message");
        }
    }
}
