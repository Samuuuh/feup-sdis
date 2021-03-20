package message;

import main.Peer;

import java.util.Arrays;

/**
 * Design pattern: builder.
 * Class responsible for building the messages according to the description.
 * https://refactoring.guru/pt-br/design-patterns/builder
 */
public class MessageBuilder {
    String header;

    public MessageBuilder(String type, String fileId){
        buildCommonHeader(type, fileId);
    }


    private void buildCommonHeader(String type, String fileId){
        header = Peer.version + " " + type + " " + Peer.peer_no + " " + fileId;
    }

    public void addChunkNo(String chunkNo){
        header += " " + chunkNo;
    }

    public void addRepDeg(String repDeg){
        header += " " + repDeg;
    }

    public byte[] buildWithBody(byte[] bodyBytes){
        header += "\r\n";
        byte[] headerBytes = header.getBytes();
        byte[] merge = Arrays.copyOf(headerBytes, headerBytes.length + bodyBytes.length);
        System.arraycopy(bodyBytes, 0, merge, headerBytes.length, bodyBytes.length);
        return merge;
    }

    public byte[] build(){
        header += "\r\n";
        return header.getBytes();
    }



}
