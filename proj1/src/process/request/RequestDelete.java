package process.request;

import main.Peer;
import main.etc.Logger;
import main.etc.Singleton;
import send.SendDelete;

public class RequestDelete extends Thread {
    protected String fileName;

    public RequestDelete(String fileName){
        this.fileName = fileName;
    }

    @Override
    public void run() {

        String fileId = Singleton.hash(fileName);
        try {
            // TODO: Change the place of this
            Peer.peer_state.removeFile(fileId);

            for(int i = 0; i < 5; i++) {
                Thread.sleep(500);
                new SendDelete(Singleton.DELETE, fileId, Peer.mc_addr, Peer.mc_port).start();
                Logger.REQUEST(this.getClass().getName(), "Requested DELETE on " + fileId + " ATTEMPT No. " + i);
            }
        } catch (InterruptedException e) {
            Logger.ERR(this.getClass().getName(), "Failed REQUESTING DELETE " + fileId);
            e.printStackTrace();
        }
    }
}
