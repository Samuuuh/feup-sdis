package service.node;

import java.util.concurrent.ConcurrentHashMap;

public class Chord {
  public int id;
  private String ip;
  private String port;
  // TODO: Ask about Integer or BigInteger.
  private ConcurrentHashMap<Integer,  ChordInfo> fingerTable = new ConcurrentHashMap<>();
  public Chord(String ip, String port) {
    this.id = 1; // ID MOD M

    this.ip = ip;
    this.port = port;
  }

  public void join(String ip, String port){
    // TPCChannel
  } 

  public void startFingers(){

  }

  public void leaveRing(){
      // TODO
  }

  public void registerChordNode(){
     // TODO
  }
}