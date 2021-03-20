package factory;


import main.Peer;

import java.io.*;

public abstract class MessageFactory {
    protected String fileId;
    protected String type;

    public MessageFactory(String type, String fileId) {
        this.type = type;
        this.fileId = fileId;
    }

    public byte[] generateHeader() {
        // TODO : Fix the version.

        String version = "1.0";
        String header = version + " " + type + " " + Peer.peer_no + " " + fileId + "\r\n";
        System.out.println("HEADER " + header);
        return header.getBytes();
    }
}
