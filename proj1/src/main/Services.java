package main;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Services extends Remote {
    // TODO: Create "RESTORE", .... and implement on PEER
    String backup(String filePath, int replicationDeg) throws IOException;

    String restore(String filePath) throws IOException;

    String restoreEnhance(String filePath) throws IOException;

    String delete(String filePath) throws IOException;

    String reclaim(String space) throws IOException;
}
