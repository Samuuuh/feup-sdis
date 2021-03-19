package state;

import main.Peer;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class SaveState extends Thread {
    private final String filePath = "/peers/" + Peer.peer_no + "/savedState/";


    @Override
    public void run() {
        try {
            String fileName = "state.ser";

            /*Path path = Paths.get(this.filePath);
            Files.createDirectories(path);*/
            File file = new File(fileName);

            if (!file.exists()) {
                file.createNewFile();
            }


            FileOutputStream fileStream = new FileOutputStream(fileName);
            ObjectOutputStream out = new ObjectOutputStream(fileStream);
            out.writeObject(Peer.peer_state);
            out.close();

        } catch (IOException i) {
            i.printStackTrace();
        }
    }
}
