package state;

import main.Definitions;
import main.Peer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SaveState extends Thread {


    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(5 * 1000);

                String pathString = Definitions.getStatePath(Peer.peer_no);
                String filePathString = pathString + Definitions.STATE_FILE_NAME;

                Path path = Paths.get(pathString);
                Files.createDirectories(path);

                FileOutputStream fileOutputStream = new FileOutputStream(filePathString);
                ObjectOutputStream objectOutputStream = new ObjectOutputStream(fileOutputStream);
                objectOutputStream.writeObject(Peer.peer_state);
                objectOutputStream.close();
                fileOutputStream.close();

                Peer.peer_state.printState();
            } catch (IOException | InterruptedException i) {
                i.printStackTrace();
            }
        }
    }
}
