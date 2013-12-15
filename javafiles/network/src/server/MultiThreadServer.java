package server;
import java.io.*;
import java.net.*;

class ServerThread implements Runnable {

  private Socket socket;
  private BufferedWriter out;
  private BufferedReader in;

  public ServerThread(Socket socket){
    this.socket=socket;
  }

  public void run(){
     try{
        
        out =new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
        in = new BufferedReader(new InputStreamReader (socket.getInputStream()));
        out.write("What is your name?\n");
        out.flush();
        String  line, name = in.readLine();
        out.write("Hello, "+name+"!\n");
        out.flush();

        while (( line=in.readLine())!=null)
        {
           
        	if (!line.equals("Bye")){
        		
            out.write("You say "+line+"\n"); 
            out.flush();
            
            
           }
           else{   // client said Bye
            out.write("Bye\n"); 
            out.flush();
             break;
           }
       }
        
        out.close();
        in.close();
        socket.close();

    } catch (IOException e){
        e.printStackTrace();
      }
  }   
}

public class MultiThreadServer{

  public static void main(String [] args){
    ServerSocket ssock;
 
    try{
      ssock = new ServerSocket(4000);
      System.out.println("I am online");
      
      while (true){
        Socket sock = ssock.accept(); 
        System.out.println("Connection accepted");
        ServerThread st=new ServerThread(sock);
		Thread t=new Thread(st); 
        t.start();
        }
    } catch (IOException e){
        System.out.println("Could not connect.");
        System.exit(1);        
     }

  }
}