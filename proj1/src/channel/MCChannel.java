package channel;

import main.etc.Logger;
import main.etc.Singleton;
import main.Peer;
import process.DeleteSingleChunk;
import process.answer.PrepareChunk;
import process.DeleteChunks;
import process.RemoveCheck;
import process.request.RequestDeleteOnBoot;
import process.request.RequestDeleteOnRepDeg;
import send.SendWithFileId;
import state.ChunkState;

import java.io.IOException;
import java.net.DatagramPacket;

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
                messageParsed = new MessageParser(packet.getData(), packet.getLength());

                if (messageParsed.getSenderId().equals(Peer.peer_no))
                    continue;

                String fileId = messageParsed.getFileId();
                String chunkId = Singleton.getChunkId(fileId, messageParsed.getChunkNo());

                if (messageParsed.getMessageType().equals(Singleton.STORED))
                    handleStored(messageParsed, chunkId);


                else if (messageParsed.getMessageType().equals(Singleton.GETCHUNK)) {
                    if (Peer.version.equals(Singleton.VERSION_ENH))
                        new PrepareChunk(packet.getAddress(), chunkId, messageParsed.getTcpPort()).start();
                    else
                        new PrepareChunk(packet.getAddress(), chunkId).start();
                }

                else if (messageParsed.getMessageType().equals(Singleton.DELETE))
                    handleDelete(messageParsed, chunkId);

                else if (messageParsed.getMessageType().equals(Singleton.REMOVED)) {
                    Logger.INFO(this.getClass().getName(), "Received" + Singleton.REMOVED + "from peer " + messageParsed.getSenderId() + " chunk " + chunkId);
                    new RemoveCheck(fileId, chunkId, messageParsed.getSenderId()).start();

                } else if (Peer.version.equals(Singleton.VERSION_ENH) && messageParsed.getMessageType().equals(Singleton.BOOT)) {
                    new RequestDeleteOnBoot(messageParsed.getSenderId()).start();

                } else if (Peer.version.equals(Singleton.VERSION_ENH) && messageParsed.getMessageType().equals(Singleton.SINGLEDELETEFILE)) {
                    Logger.INFO(this.getClass().getName(), "Received " + Singleton.SINGLEDELETEFILE);
                    if (!messageParsed.getDestinationId().equals(Peer.peer_no)) continue;
                    new DeleteChunks(fileId).start();

                } else if (Peer.version.equals(Singleton.VERSION_ENH) && messageParsed.getMessageType().equals(Singleton.RCVDELETE)) {
                    Logger.INFO(this.getClass().getName(), "Received" + Singleton.RCVDELETE + "from peer " + messageParsed.getSenderId());
                    Peer.peer_state.removeFileToDelete(messageParsed.getSenderId(), messageParsed.getFileId());

                } else if (Peer.version.equals(Singleton.VERSION_ENH) && messageParsed.getMessageType().equals(Singleton.SINGLEDELETECHUNK)) {
                    Logger.INFO(this.getClass().getName(), "Received " + Singleton.SINGLEDELETECHUNK + " for peer " + messageParsed.getDestinationId() + " chunk " + chunkId);
                    Peer.peer_state.removePeerOfChunk(chunkId, messageParsed.getDestinationId());
                    Peer.peer_state.removePeerOfFileChunk(chunkId, messageParsed.getDestinationId());

                    if (messageParsed.getDestinationId().equals(Peer.peer_no)) {
                        new DeleteSingleChunk(chunkId).start();
                    }
                }

            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    private void handleStored(MessageParser messageParsed, String chunkId) {
        Peer.peer_state.updateChunkState(chunkId, messageParsed.getSenderId());
        Peer.peer_state.updateFileState(messageParsed.getFileId(), messageParsed.getChunkNo(), messageParsed.getSenderId());

        if (Peer.version.equals(Singleton.VERSION_ENH)) {
            new RequestDeleteOnRepDeg(chunkId, messageParsed.getSenderId()).start();
            cancelStoreChunk(chunkId);
        }
        Logger.SUC(this.getClass().getName(), "STORED " + chunkId + " on PEER " + messageParsed.getSenderId());
    }

    private void handleDelete(MessageParser messageParsed, String chunkId){
        String fileId = messageParsed.getFileId();
        if (Peer.version.equals(Singleton.VERSION_ENH))
            new SendWithFileId(Singleton.RCVDELETE, fileId, Peer.mc_addr, Peer.mc_port).start();
        Peer.peer_state.removeChunkFromFileState(fileId, chunkId);
        new DeleteChunks(messageParsed.getFileId()).start();
    }

    private void cancelStoreChunk(String chunkId) {
        ChunkState chunkState = Peer.peer_state.getChunkState(chunkId);
        if (chunkState != null && chunkState.haveDesiredRepDeg() && !chunkState.contains(Peer.peer_no))  {
            Peer.peer_state.removeChunk(chunkId);
            Peer.storeTasks.abortTask(chunkId);
        }
    }


}
