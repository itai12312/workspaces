package remoteControllreversed;
import java.io.*;
import java.net.*;
//get image-like vnc
//http://en.wikipedia.org/wiki/User_Datagram_Protocol udp protocol
//get correct mouse button
//servers should able to message speciifc clients withou them staring converstion-put in a new seprate thread
class NetworkRead implements Runnable{
	private Socket socket;
	ServerTextArea tx;
	Client cl;
	public NetworkRead(Socket socket,ServerTextArea tx,Client cl){
		this.socket=socket;
		this.tx=tx;
		this.cl=cl;
	}
	public void run(){
		if(cl.getConnected()){
			try{
				BufferedReader in= new BufferedReader( new InputStreamReader(socket.getInputStream()));
				String serverString;
				while ((serverString=in.readLine())!=null) {

					tx.write(/*"message recieved from"+socket.getInetAddress()+":"+*/serverString+"\n");          //Server says...	
				}
			}catch(IOException e){
				e.printStackTrace();
			}
			tx.write("Disconnected from server"+"\n");
			System.exit(0);
		}
	}

}public class Client implements Runnable{
	Socket socket;
	BufferedReader in;
	BufferedWriter out;
	ServerTextArea tx;
	private boolean connected=false;
	public Client(){
		this.tx=new ServerTextArea(this);
	}


	public void start(){
		Thread logRead=new Thread(this, "client");
		logRead.start();
	}

	public void run(){
		NetworkRead readThread=new NetworkRead(socket,tx,this);
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
		if(getConnected()){
			send(text);
		}
	}


	public void sendToAll(String string) {
		if(getConnected()){
			send(string);
		}
	}


	public synchronized void connect(int port, String host) {
		try{ 
			socket=new Socket(host,port);
			out=new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
		}
		catch(IOException e){
			System.err.println("Couldn't get I/O for the connection to server.");
			System.exit(1);
		}
		connected=true;
	}
	public synchronized boolean getConnected(){
		return connected;
	}


}