package process.request;

import main.Peer;
import main.etc.Singleton;
import send.SendSingleDelete;

import java.util.List;


public class RequestDeleteOnBoot extends Thread {
    String peer_no;
    public RequestDeleteOnBoot(String peer_no){
        this.peer_no = peer_no;
    }

    @Override
    public void run() {
        List<String> filesToDelete = Peer.peer_state.getFilesToDelete(peer_no);
        if (filesToDelete == null) return;

        for (String fileId: filesToDelete){
            new SendSingleDelete(Singleton.SINGLEDELETEFILE, peer_no, fileId, Peer.mc_addr, Peer.mc_port).start();
        }

        Peer.peer_state.removeDeletesOfPeer(peer_no);

    }
}
