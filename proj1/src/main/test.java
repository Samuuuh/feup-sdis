package main;
import factory.BackupMessageFactory;

import java.io.IOException;

public class test {
    public static void main(String[] args) throws IOException {
        String file = args[0];
    BackupMessageFactory b = new BackupMessageFactory(file, "123456", 1);
    byte[] bla = b.createMessage();
    System.out.println(new String(bla));
    }

}
