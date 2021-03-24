package channel;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;

public abstract class Channel extends Thread {
    protected final String mcast_addr;
    protected final int mcast_port;
    protected final InetAddress group;
    protected final MulticastSocket mcast_socket;
    protected MessageParser messageParsed;

    public Channel(int mcast_port, String mcast_addr) throws IOException {
        this.mcast_port = mcast_port;
        this.mcast_addr = mcast_addr;

        // Join the group. 
        mcast_socket = new MulticastSocket(mcast_port);
        this.group = InetAddress.getByName(mcast_addr);
        mcast_socket.joinGroup(group);
    }
}
