package network.server.CheckPredecessor;

import network.etc.Logger;
import network.etc.Singleton;
import network.server.fixFingers.FixFingers;

import java.util.concurrent.*;

public class CheckPredecessorOrchestrator implements Runnable {

    @Override
    public void run() {
        try {

            ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();

            while (true) {
                executorService.schedule(new CheckPredecessor(), Singleton.FIX_FINGERS_TIME, TimeUnit.SECONDS);
                executorService.awaitTermination(1, TimeUnit.SECONDS);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Logger.ERR(this.getClass().getName(), "Fatal error on checkPredecessor, finishing peer.");
            System.exit(-1);
        }
    }
}
