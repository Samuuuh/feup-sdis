package sdis.proj; 

import java.rmi.Remote;
import java.rmi.RemoteException;

interface Services extends Remote{
    String backup(); 
}
