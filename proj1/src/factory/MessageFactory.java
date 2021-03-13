package factory;


import main.Peer;

import java.io.*;

abstract public class MessageFactory {
    protected String fileId;
    protected String type;
    protected String senderId;

    public MessageFactory(String type, String fileId) {
        this.type = type;
        this.fileId = fileId;
    }


    protected byte[] generateHeader() {
        // TODO : to fix the version. How to store the version of a file?

        String version = "1.0";
        String header = version + " " + type + " " + Peer.peer_no + " " + fileId + "\r\n";
        System.out.println("HEADER " + header);
        return header.getBytes();
    }
}
