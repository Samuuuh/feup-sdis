package build.proj; 

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Services extends Remote{
    public abstract String backup() throws RemoteException; 
}
