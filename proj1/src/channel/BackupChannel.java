package channel;
import file.Chunk;
import main.Definitions;
import subProtocol.BackupSubProtocol;
import java.net.DatagramPacket;
import factory.MessageParser;

import java.io.IOException;

public class BackupChannel extends Channel {

    /**
     * <Version> PUTCHUNK <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF><CRLF><Body>
     * <Version> STORED <SenderId> <FileId> <ChunkNo> <CRLF><CRLF>
     */
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
                messageParsed = new MessageParser(packet.getData());

                putChunk();

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }


    void putChunk() {
        String filePath = "";
        String fileId = "";
        String senderId = "";
        int replicationDeg = 0;
        Chunk[] chunks = new Chunk[0];

        System.out.println("Chegou aqui cariou");
        BackupSubProtocol asd = new BackupSubProtocol(filePath, fileId, senderId, replicationDeg, chunks);
        //asd.start();
    }

    void handleStoredMessage() {
        // Ler
    }
}
