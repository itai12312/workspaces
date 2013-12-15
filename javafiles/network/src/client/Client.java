package client;

import java.io.*;
import java.net.*;


class NetworkRead implements Runnable{
  private Socket socket;
  

 public NetworkRead(Socket socket){
   this.socket=socket;
  
 }
 public void run(){
   try{
   
    BufferedReader in= new BufferedReader( new InputStreamReader(socket.getInputStream()));
    String serverString;
   while ((serverString=in.readLine())!=null)
   {
     System.out.println(serverString);          //Server says...
   }
  }catch(IOException e){
	  e.printStackTrace();
  }
  System.out.println("Disconnected from server");
  System.exit(0);
 }
}
public class Client implements Runnable{
Socket socket;
BufferedReader in;
BufferedWriter out;

public Client(){
  try{ 
    socket=new Socket("MEG7",4000);
    out=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
    
    }
  catch(IOException e){
     System.err.println("Couldn't get I/O for the connection to server.");
     System.exit(1);
  }
}


public void start(){
      Thread logRead=new Thread(this, "client");
      logRead.start();
}

public void run(){
 try{
  BufferedReader keyboard=new BufferedReader(new InputStreamReader(System.in));
  String serverString;
  String userString;
  NetworkRead readThread=new NetworkRead(socket);
  Thread read=new Thread(readThread,"read");
  read.start();
  boolean degel=true;
    while (degel)
    {
      String line=keyboard.readLine();
      out.write(line+"\n");
      out.flush();
	}
   
}catch (IOException e){
       System.err.println("Couldn't read or write");
       System.exit(1);
       }
}

public static void main(String [] arg){
 Client t=new Client();
     t.start();
 }
}
