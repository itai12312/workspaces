package remotecontrol2;
import java.io.*;
import java.net.*;
//get image-like vnc
//servers should able to message speciifc clients withou them staring converstion-put in a new seprate thread
//http://en.wikipedia.org/wiki/User_Datagram_Protocol udp protocol
class NetworkRead implements Runnable{
	private Socket socket;
	ServerTextArea tx;
	public NetworkRead(Socket socket,ServerTextArea tx){
		this.socket=socket;
		this.tx=tx;
	}
	public void run(){
		try{
			BufferedReader in= new BufferedReader( new InputStreamReader(socket.getInputStream()));
			String serverString;
			while ((serverString=in.readLine())!=null) {
				//get pic from server
				if(serverString.equals("startpic")){
					while(serverString!=null&&!serverString.equals("endpic")){
						createpic(serverString);
					}
					tx.paint("ss");//set pic as background-paint image here and set client backgorund as servers picture(roles still reversed)...
					
				}
				tx.write(/*"message recieved from"+socket.getInetAddress()+":"+*/serverString+"\n");          //Server says...	
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		tx.write("Disconnected from server"+"\n");
		System.exit(0);
	}
	private  createpic(String serverString) {
		if()
	}
}
public class Client implements Runnable{
	Socket socket;
	BufferedReader in;
	BufferedWriter out;
	ServerTextArea tx;
	public Client(){
		this.tx=new ServerTextArea(this);
		try{ 
			socket=new Socket("AZ-PC",4000);
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
			//tx.write("send from"+socket.getInetAddress().getHostName()+" "+s+"   in send method");
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
	public void sentToSpecificSocker(String text, int port, String host) {
		send(text);
	}
	public void sendToAll(String string) {
		send(string);
	}
}