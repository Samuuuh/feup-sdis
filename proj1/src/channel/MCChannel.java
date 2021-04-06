package channel;

import main.etc.Logger;
import main.etc.Singleton;
import main.Peer;
import process.answer.PrepareChunk;
import process.DeleteChunks;
import process.RemoveCheck;
import process.request.RequestDeleteOnBoot;
import send.SendWithFileId;
import state.ChunkState;

import java.io.IOException;
import java.net.DatagramPacket;

// STORED, GETCHUNK, DELETE, REMOVE
public class MCChannel extends Channel {
    public MCChannel(int mcast_port, String mcast_addr) throws IOException {
        super(mcast_port, mcast_addr);
    }

    @Override
    public void run() {
        while (true) {
            try {
                byte[] buf = new byte[83648];
                DatagramPacket packet = new DatagramPacket(buf, buf.length, group, mcast_port);
                mcast_socket.receive(packet);
                messageParsed = new MessageParser(packet.getData());
                if (messageParsed.getSenderId().equals(Peer.peer_no))
                    continue;

                String fileId = messageParsed.getFileId();
                String chunkId = Singleton.getChunkId(fileId, messageParsed.getChunkNo());
                if (messageParsed.getMessageType().equals(Singleton.STORED) ) {
                    Peer.peer_state.updateChunkState(chunkId, messageParsed.getSenderId());

                    // Operation just possible for peer that has requested a backup for that file.
                    Peer.peer_state.updateFileState(fileId, messageParsed.getChunkNo(), messageParsed.getSenderId());
                    System.out.println(Peer.peer_state.getChunkState(chunkId));
                    //Peer.peer_state.printState();
                    cancelStoreChunk(chunkId);
                    Logger.SUC(this.getClass().getName(), "STORED " + chunkId + " on PEER " + messageParsed.getSenderId());
                }

                else if (messageParsed.getMessageType().equals(Singleton.GETCHUNK)) {
                    new PrepareChunk(chunkId).start();
                }

                else if (messageParsed.getMessageType().equals(Singleton.DELETE)) {
                    //if version 2
                    new SendWithFileId(Singleton.RCVDELETE, fileId, Peer.mc_addr, Peer.mc_port).start();
                    new DeleteChunks(messageParsed.getFileId()).start();

                }

                else if (messageParsed.getMessageType().equals(Singleton.REMOVED)){
                    if (Peer.peer_state.getFileState(fileId) != null)
                        new RemoveCheck(fileId, chunkId, messageParsed.getSenderId()).start();
                }
                else if (messageParsed.getMessageType().equals(Singleton.BOOT)){
                    System.out.println("RECEIVED BOOT " + messageParsed.getSenderId() );
                    new RequestDeleteOnBoot(messageParsed.getSenderId()).start();

                } else if (messageParsed.getMessageType().equals(Singleton.SINGLEDELETE)){
                    if (!messageParsed.getDestinationId().equals(Peer.peer_no)) continue;
                    System.out.println("RECEIVED SINGLE DELETE ");
                    new DeleteChunks(messageParsed.getFileId()).start();
                }else if (messageParsed.getMessageType().equals(Singleton.RCVDELETE)){
                    // TODO: if version 2.
                    Logger.INFO(this.getClass().getName(), "RCVDELETE from peer " + messageParsed.getSenderId());
                    Peer.peer_state.removeFileToDelete(messageParsed.getSenderId(), messageParsed.getFileId());
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void cancelStoreChunk(String chunkId){
        ChunkState chunkState = Peer.peer_state.getChunkState(chunkId);
        if (chunkState != null && chunkState.haveDesiredRepDeg()) {
            Peer.storeTasks.abortTask(chunkId);
        }
    }

}
