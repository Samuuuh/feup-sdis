package channel;
import factory.MessageParser;
import main.Definitions;
import main.Peer;

import java.io.File;
import java.net.DatagramPacket;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, mcast_port);
                receivePacket(mcast_socket, packet);

                System.out.println("BackupChannel\t:: Packet received.");
                messageParsed = new MessageParser(packet.getData());

                // Checks if message came from the same peer.
                if (messageParsed.getSenderId().equals(Peer.peer_no))
                    continue;

                // Treats the message.
                if (messageParsed.getMessageType().equals(Definitions.PUTCHUNK))
                    putChunk(messageParsed);

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    void putChunk(MessageParser messageParsed) {
        System.out.println("BackupChannel\t:: Treating PUTCHUNK...");
        // TODO: Save files.
        saveFile(messageParsed);

        // TODO: Send message of success or error.
        // BackupSubProtocol asd = new BackupSubProtocol(filePath, fileId, senderId, replicationDeg, chunks);
        //asd.start();
    }

    void saveFile(MessageParser messageParsed){

        System.out.println("BackupChannel\t:: Saving file " + messageParsed.getFileId() + "...");

        try {
            Path path = Paths.get("savedFiles");

            Files.createDirectories(path);
            File file = new File("savedFiles/" + messageParsed.getFileId());
            file.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

}
