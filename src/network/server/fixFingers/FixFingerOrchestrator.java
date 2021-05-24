package network.server.fixFingers;

import network.etc.Singleton;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class FixFingerOrchestrator implements Runnable {

    @Override
    public void run() {
        try {
            for (int i = 0; i < Singleton.m; i++) {
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.submit(new FixFingers());
                executorService.awaitTermination(500, TimeUnit.MILLISECONDS);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
