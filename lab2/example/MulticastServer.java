import java.io.*;

public class MulticastServer {
    public static void main(String[] args) throws IOException {
        //new Server().start();
        new MulticastServerThread().start();
    }
}