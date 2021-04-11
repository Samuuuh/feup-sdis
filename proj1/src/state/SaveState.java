package state;

import main.etc.Singleton;
import main.Peer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Thread saves the state after each x seconds.
 */
public class SaveState extends Thread {

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(Singleton.SAVE_PERIOD * 1000L);

                Peer.peer_state.saveState();
            } catch (IOException | InterruptedException i) {
                i.printStackTrace();
            }
        }
    }
}
