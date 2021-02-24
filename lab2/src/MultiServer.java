import java.io.*;
import java.net.*;
import java.util.*;

public class MultiServer {
    public static void main(String[] args) throws java.io.IOException {
        new MultiServerThread(args).start();
    }
}