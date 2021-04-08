package main;

import state.State;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Services extends Remote {
    String backup(String filePath, int replicationDeg) throws IOException;

    String restore(String filePath) throws IOException;

    String delete(String filePath) throws IOException;

    String reclaim(String space) throws IOException;

    State state() throws  IOException;
}
