package main;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Services extends Remote{
    public static String backup() throws RemoteException {
        return null;
    }
}
