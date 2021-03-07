package factory;
import main.Definitions;
import java.io.IOException;
import java.util.Arrays;
import file.FileHandler;

// Creates messages requesting backup.
public class BackupMessageFactory extends MessageFactory{
    protected int chunkNo;
    public BackupMessageFactory(String filePath, String senderId, int repDeg) {
        super(filePath, Definitions.PUTCHUNK, senderId, repDeg);
        chunkNo = 0;
    }

    @Override
    public byte[] createMessage() throws IOException {
        byte[] header = generateHeader();
        byte[] fileContent = FileHandler.readFile(filePath);


        // TODO: how to handle this.
        byte[] both = Arrays.copyOf(header, header.length + fileContent.length);
        System.arraycopy(fileContent, 0, both, header.length, fileContent.length);
        chunkNo++;

        return both;
    }

    @Override
    protected byte[] generateHeader() {
        // TODO : to fix the version.
        String version = "1.0";
        // TODO: will the replication degree be the same for all the headers for a file?
        String header = version + " " + Definitions.PUTCHUNK + " " + senderId + " " + fileId + " " +  chunkNo + " " + repDeg + "\r\n";
        return header.getBytes();
    }
}
