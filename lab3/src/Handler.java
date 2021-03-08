package build.lab3;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Handler extends Remote {
    String handleRequest(String request) throws RemoteException;
}