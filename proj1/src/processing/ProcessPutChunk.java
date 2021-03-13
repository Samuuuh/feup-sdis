package processing;

import factory.MessageParser;
import main.Definitions;
import sendMessage.SendMessageWithChunkNo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Random;

// TODO: close file after writing.
public class ProcessPutChunk extends Thread{
    MessageParser messageParsed;
    public ProcessPutChunk(MessageParser messageParsed){
        this.messageParsed = messageParsed;
    }

    @Override
    public void run(){
        System.out.println("ProcessPutChunk\t:: Treating PUTCHUNK...");

        // Backup file only reads and redirect data
        if (saveFile(messageParsed)) {
            new SendMessageWithChunkNo(messageParsed.getVersion(), Definitions.STORED, messageParsed.getFileId(), messageParsed.getChunkNo()).start();
        }
        else {
            System.out.println("ProcessPutChunk\t:: Error saving file");
        }
    }

    Boolean saveFile(MessageParser messageParsed){

        System.out.println("ProcessPutChunk\t:: Saving file " + messageParsed.getFileId());

        Random rand = new Random();
        String filePath = "savedFiles/" + messageParsed.getFileId() + rand.nextInt(1000);

        try {
            Path path = Paths.get("savedFiles");
            Files.createDirectories(path);
            File file = new File(filePath);

            if (file.exists()){
                FileOutputStream outputFile = new FileOutputStream(filePath, true);
                outputFile.write(messageParsed.getData());
                outputFile.close();
            }else{
                file.createNewFile();
            }

            FileOutputStream outputStream = new FileOutputStream(filePath);
            outputStream.write(messageParsed.getData());
            outputStream.close();

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }

}
