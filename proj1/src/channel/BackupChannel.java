package channel;
import main.Definitions;

import java.io.IOException;

public class BackupChannel extends Channel {

    public BackupChannel(int mcast_port, String mcast_addr) throws IOException {
        super(mcast_port, mcast_addr);
    }

}
