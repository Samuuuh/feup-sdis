package factory;

import main.Peer;

public class MessageChunkNo extends MessageFactory {
        String chunkNo;
        public MessageChunkNo(String type, String fileId, String chunkNo) {
            super(type, fileId);
            this.chunkNo = chunkNo;
        }

        @Override
        public byte[] generateHeader() {
            // TODO : to fix the version.
            String version = "1.0";
            String header = version + " " + type + " " + Peer.peer_no + " " + fileId + " " +  chunkNo + "\r\n";
            System.out.println("HEADER " + header);
            return header.getBytes();
        }
}
