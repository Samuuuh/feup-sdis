package factory;

import main.Peer;

/**
 * Supported type of messages: STORED, GETCHUNK, REMOVE.
 */
public class MessageChunkNo extends MessageFactory {
        String chunkNo;
        public MessageChunkNo(String type, String fileId, String chunkNo) {
            super(type, fileId);
            this.chunkNo = chunkNo;
        }

    @Override
    public byte[] createMessage() {
        return createHeader();
    }

    @Override
        protected byte[] createHeader() {
            String version = "1.0";
            String header = version + " " + type + " " + Peer.peer_no + " " + fileId + " " +  chunkNo + "\r\n";
            System.out.println("HEADER " + header);
            return header.getBytes();
        }
}
