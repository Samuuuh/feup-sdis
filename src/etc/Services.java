package etc;

import java.io.IOException;
import java.rmi.Remote;

public interface Services extends Remote {
    public void lookup() throws IOException;
}