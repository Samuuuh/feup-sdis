package main;

public class Definitions {

    public static final int CHUNK_MAX_SIZE = 64000;
    public static final int REGISTER_PORT = 1888;

    // Type Messages.
    // <Version> PUTCHUNK <SenderId> <FileId> <ChunkNo> <ReplicationDeg> <CRLF><CRLF><Body>
    public static String PUTCHUNK = "PUTCHUNK";

    // <Version> STORED <SenderId> <FileId> <ChunkNo> <CRLF><CRLF
    public static String STORED = "STORED";
    public static String GETCHUNK = "GETCHUNK";
    public static String REMOVED = "REMOVED";

    // <Version> CHUNK <SenderId> <FileId> <ChunkNo> <CRLF><CRLF><Body>
    public static String CHUNK = "CHUNK";

    // <Version> DELETE <SenderId> <FileId> <CRLF><CRLF>
    public static String DELETE = "DELETE";


    // State
    public static String STATE_FILE_NAME = "state.ser";
    public static String getStatePath(String peer_no){
        return  "peers/" + peer_no + "savedState/";
    }



}
