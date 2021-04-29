package node;

public class Chord {
  public int id;
  private String ip;
  private String port;

  public Chord(String ip, String port) {
    this.id = 1; // ID MOD M

    this.ip = ip;
    this.port = port;
  }

  public void join(String ip, String port){
    // TPCChannel
  } 

  public void startFingers(){
      // TODO
  } 

  public void leaveRing(){
      // TODO
  }

  public void registerChordNode(){
     // TODO
  }
}