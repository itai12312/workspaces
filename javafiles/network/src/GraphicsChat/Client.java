package GraphicsChat;

import java.io.*;
import java.net.*;


class NetworkRead implements Runnable{
  private Socket socket;
  ClientTextArea tx;

 public NetworkRead(Socket socket,ClientTextArea tx){
   this.socket=socket;
  this.tx=tx;
 }
 public void run(){
   try{
   
    BufferedReader in= new BufferedReader( new InputStreamReader(socket.getInputStream()));
    String serverString;
   while ((serverString=in.readLine())!=null) {
     tx.write("message recieved from"+socket.getInetAddress()+":"+serverString);          //Server says...
   }
  }catch(IOException e){
	  e.printStackTrace();
  }
  tx.write("Disconnected from server");
  System.exit(0);
 }
}
public class Client implements Runnable{
Socket socket;
BufferedReader in;
BufferedWriter out;
ClientTextArea tx;
public Client(){
	this.tx=new ClientTextArea(this);
  try{ 
    socket=new Socket("MEG15",4000);
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
  NetworkRead readThread=new NetworkRead(socket,tx);
  Thread read=new Thread(readThread,"read");
  read.start();
}

public void send(String s){
	try {
		tx.write("send from"+socket.getInetAddress().getHostName()+" "+s+"   in send method");
		out.write(s+"\n");
		out.flush();
	} catch (IOException e) {
		e.printStackTrace();
	}
}

public static void main(String [] arg){
	 Client t=new Client();
     t.start();
 }
}