package main;
import file.Chunk;
import file.FileHandler;

import java.io.IOException;

public class Test {

    public static void main(String[] args) throws IOException {
        byte[] content = FileHandler.readFile(args[0]);
        Chunk[] chunk = FileHandler.splitFile(content);


        for (Chunk value : chunk) {
            System.out.println(new String(value.getChunkData()));
        }
    }
}
