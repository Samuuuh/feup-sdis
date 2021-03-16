package factory;

import channel.BackupChannel;
import main.Peer;

import java.io.IOException;

public class MessageChunkTemp extends MessageFactory {
        String chunkNo;
        public MessageChunkTemp(String type, String fileId, String chunkNo) {
            super(type, fileId);
            this.chunkNo = chunkNo;

        }

        public byte[] generateHeader() {
            // TODO : to fix the version. How to store the version of a file?

            String version = "1.0";
            String header = version + " " + type + " " + Peer.peer_no + " " + fileId + " " +  chunkNo + "\r\n";
            System.out.println("HEADER " + header);
            return header.getBytes();
        }
}
