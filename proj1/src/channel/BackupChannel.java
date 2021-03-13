package channel;
import factory.MessageParser;
import main.Definitions;
import main.Peer;
import java.net.DatagramPacket;

import java.io.IOException;

public class BackupChannel extends Channel {

    public BackupChannel(int mcast_port, String mcast_addr) throws IOException {
        super(mcast_port, mcast_addr);

        // TODO: Como escolhemos os computadores que vao fazer backup?
        // Guardamos em todos os computadores da rede se o repetition degree < terminals.
    }


    @Override
    public void run() {
        byte[] buf = new byte[Definitions.CHUNK_MAX_SIZE];

        while (true) {
            try {
                // TODO: quando for enviada a mensagem multicast, o peer nao vai receber mensagem dele mesmo questionmark
                // Se o sender Id for igual nao fazemos nada.
                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, mcast_port);
                receivePacket(mcast_socket, packet);

                System.out.println("BackupChannel\t:: Packet received.");
                messageParsed = new MessageParser(packet.getData());

                // Checks if message came from the same peer.
                if (messageParsed.getSenderId().equals(Peer.peer_no))
                    continue;

                // Treats the message.
                if (messageParsed.getMessageType().equals(Definitions.PUTCHUNK))
                    putChunk();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    void putChunk() {
        System.out.println("BackupChannel\t:: Treating PUTCHUNK...");
        // Save files.

        // TODO: Send message of success or error.
        // BackupSubProtocol asd = new BackupSubProtocol(filePath, fileId, senderId, replicationDeg, chunks);
        //asd.start();
    }

    void handleStoredMessage() {
        // Ler
    }
}
