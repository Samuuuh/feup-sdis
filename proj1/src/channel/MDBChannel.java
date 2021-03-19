package channel;
import factory.MessageParser;
import main.Definitions;
import main.Peer;
import processing.PutChunk;
import java.net.DatagramPacket;

import java.io.IOException;

// TODO: MDB
public class MDBChannel extends Channel {
    public MDBChannel(int mcast_port, String mcast_addr) throws IOException {
        super(mcast_port, mcast_addr);

        // TODO: Como escolhemos os computadores que vao fazer backup?
        // Guardamos em todos os computadores da rede se o repetition degree < terminals.
    }


    @Override
    public void run() {
        while (true) {
            try {
                byte[] buf = new byte[83648];
                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, mcast_port);
                receivePacket(mcast_socket, packet);

                System.out.println("BackupChannel\t:: Packet received."); // Receive PutChunk
                messageParsed = new MessageParser(packet.getData());

                // Checks if message came from the same peer.
                if (messageParsed.getSenderId().equals(Peer.peer_no))
                    continue;

                // Treats the message.
                if (messageParsed.getMessageType().equals(Definitions.PUTCHUNK))
                    new PutChunk(messageParsed).start();


            } catch (Exception e) {
                e.printStackTrace();
            }
        }


    }




}
