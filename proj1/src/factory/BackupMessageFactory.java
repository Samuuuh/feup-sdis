package factory;
import file.Chunk;
import main.Definitions;
import java.io.IOException;
import java.util.Arrays;
import file.FileHandler;

// Creates messages requesting backup.
public class BackupMessageFactory extends MessageFactory{
    Chunk chunk;
    public BackupMessageFactory(String filePath, String senderId, int repDeg, Chunk chunk) {
        super(filePath, Definitions.PUTCHUNK, senderId, repDeg);
        this.chunk = chunk;
    }

    @Override
    public byte[] createMessage() throws IOException {
        byte[] header = generateHeader();
        byte[] fileContent = chunk.getChunkData();

        // Concatenate.
        byte[] both = Arrays.copyOf(header, header.length + fileContent.length);
        System.arraycopy(fileContent, 0, both, header.length, fileContent.length);

        return both;
    }

    @Override
    protected byte[] generateHeader() {
        // TODO : to fix the version.
        String version = "1.0";
        // TODO: will the replication degree be the same for all the headers for a file?
        String header = version + " " + Definitions.PUTCHUNK + " " + senderId + " " + fileId + " " +  chunk.getChunkNo() + " " + repDeg + "\r\n";
        return header.getBytes();
    }
}
