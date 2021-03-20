package factory;


import main.Peer;

import java.io.*;
import java.util.Arrays;

public abstract class MessageFactory {
    protected String fileId;
    protected String type;

    public MessageFactory(String type, String fileId) {
        this.type = type;
        this.fileId = fileId;
    }

    public abstract byte[] createMessage();

    protected byte[] createHeader() {
        // TODO: fix version.
        String version = "1.0";
        String header = version + " " + type + " " + Peer.peer_no + " " + fileId + "\r\n";
        System.out.println("HEADER " + header);
        return header.getBytes();
    }

    protected byte[] mergeHeaderBody(byte[] header, byte[] body){
        byte[] merge = Arrays.copyOf(header, header.length + body.length);
        System.arraycopy(body, 0, merge, header.length, body.length);
        return merge;
    }
}
