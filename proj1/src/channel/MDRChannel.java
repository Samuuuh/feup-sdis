package channel;

import factory.MessageParser;
import main.Definitions;
import main.Peer;
import processing.ProcessPutChunk;

import java.io.IOException;
import java.net.DatagramPacket;

// TODO: MDR
public class RestoreChannel extends Channel {

    public RestoreChannel(int mcast_port, String mcast_addr) throws IOException {
        super(mcast_port, mcast_addr);

        // TODO: Como escolhemos os computadores que vao fazer backup?
        // Guardamos em todos os computadores da rede se o repetition degree < terminals.
    }


    @Override
    public void run() {
        while (true) {
            try {
                /*
                byte[] buf = new byte[83648];
                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, mcast_port);
                receivePacket(mcast_socket, packet);

                System.out.println("RestoreChannel\t:: Packet received.");
                */
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

}
