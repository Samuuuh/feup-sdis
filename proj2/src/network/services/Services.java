package network.services;

import java.io.IOException;
import java.rmi.Remote;

public interface Services extends Remote {
        /**
        * Backup Service
        * @param filePath file to be backup
        * @param repDeg Desired repdeg of the file
        */
        public String backup(String filePath, int replicationDegree) throws IOException, ClassNotFoundException;

        /**
        * Restore Service
        * @param filePath file to be restored
        */
        public String restore(String filePath) throws IOException, ClassNotFoundException;

        /**
        * Delete Service
        * @param filePath file to delete 
        */
        public String delete(String filePath) throws IOException, ClassNotFoundException;

        /**
        * Reclaim Service
        * @param targetId target id
        * @param size Size to be reclaimed
        */
        public String reclaim(String targetId, int size) throws IOException, ClassNotFoundException;
}

