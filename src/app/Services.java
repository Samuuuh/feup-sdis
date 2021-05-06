package app;

import java.io.IOException;
import java.rmi.Remote;

public interface Services extends Remote {
        public String backup(String filePath, int replicationDegree) throws IOException, ClassNotFoundException;
}

