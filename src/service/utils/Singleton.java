package service.utils;

import java.io.IOException;
import java.net.ServerSocket;

public class Singleton {

    public static int getAvailablePort() throws IOException {
        ServerSocket socket = new ServerSocket(0);
        return socket.getLocalPort();
    }
}
