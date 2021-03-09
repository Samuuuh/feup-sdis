package channel;
import main.Definitions;

import java.io.IOException;

public class BackupChannel extends Channel {

    public BackupChannel(int mcast_port, String mcast_addr) throws IOException {
        super(mcast_port, mcast_addr);
        // TODO: Como escolhemos os computadores que vao fazer backup?
        // Guardamos em todos os computadores da rede se o repetition degree < terminals.
    }

}
