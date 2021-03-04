package sdis.proj; 

import java.io.*;
import java.net.*;
import java.util.*;

import java.rmi.registry.Registry;
import java.rmi.registry.LocateRegistry;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import sdis.proj.Utils; 

public class Peer extends Services {

    public static void main(String[] args) throws IOException {
        // Parser 
        if (args.length != 1 ) {
            System.out.println("Usage:\n java Peer port");   
            return; 
        }  

        Integer port = Integer.parseInt(args[0]); 

        try{
        // Initiate class with the necessary objects. 
        Peer obj = new Peer();
        Services stub = (Services) UnicastRemoteObject.exportObject(obj, 0);   

        Registry registry = Locate 
        } cacth(Exception e){
            System.out.println("ERROR: Error while") ;
        }


    }

    private Peer(){

        
    }
   
}       