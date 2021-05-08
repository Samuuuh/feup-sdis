package service.etc;

public class Singleton {
    public static int REGISTER_PORT = 9999;
    public static int m = 4;


    public static int getRandomPortNumber(){
        int upperBound = 9000;
        int lowerBound = 7000;
        return (int)Math.floor(Math.random()*(upperBound-lowerBound+1)+lowerBound);
    }

}