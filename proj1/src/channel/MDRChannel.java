package channel;

import java.io.IOException;

// TODO: MDR
public class MDRChannel extends Channel {
    public MDRChannel(int mcast_port, String mcast_addr) throws IOException {
        super(mcast_port, mcast_addr);
    }


    @Override
    public void run() {
        /*
        while (true) {
            try {

                byte[] buf = new byte[83648];
                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, mcast_port);
                receivePacket(mcast_socket, packet);

                System.out.println("RestoreChannel\t:: Packet received.");

                System.out.println("Restore Channel ::: TO DO :::");
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        */
    }
}
