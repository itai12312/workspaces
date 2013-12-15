package RemoreControll;
import java.io.*;
import java.net.*;
import java.util.ArrayList;

class ServerThread implements Runnable {

	private Socket socket;
	private BufferedWriter out;
	private BufferedReader in;
	ServerTextArea sap;
	public ServerThread(Socket socket,ServerTextArea sta){
		this.socket=socket;
		this.sap=sta;
	}

	public void run(){
		try{

			out =new BufferedWriter( new OutputStreamWriter(socket.getOutputStream()));
			in = new BufferedReader(new InputStreamReader (socket.getInputStream()));
			out.write("What is your name?\n");
			out.flush();
			//  sap.write("send to"+socket.getInetAddress().getHostName()+" "+"What is your name?\n");
			String  line, name = in.readLine();
			sap.write("received from"+socket.getInetAddress().getHostName()+" "+name+"\n");
			out.write("Hello "+name+"!\n");
			out.flush();
			//if(!socket.isClosed()&&socket.isConnected()&&!socket.isInputShutdown()&&in!=null&&in.ready()){
			while (( line=in.readLine())!=null){
				if (!line.equals("Bye")){
					sap.write("received from"+socket.getInetAddress().getHostName()+" "+line+"\n");
					out.write("You say "+line+"\n"); 
					out.flush();
					// sap.write("sent to"+socket.getInetAddress().getHostName()+" "+"you say"+line+"\n");
				}
				else{   // client said Bye
					out.write("Bye\n"); 
					out.flush();
					// sap.write("sent to"+socket.getInetAddress().getHostName()+" "+"bye"+"\n");
					break;
				}
			}
			//}
			out.close();
			in.close();
			socket.close();
		} catch (IOException e){
			e.printStackTrace();
		}
	}   
}

public class MultiThreadServer{
	static ArrayList < Socket > list = new ArrayList< Socket >();
	ServerTextArea s;

	static ServerSocket ssock;
	public static void main(String[]args){
		MultiThreadServer p=new MultiThreadServer();
		
		//Client t=new Client();
		//t.start();
	}


	public MultiThreadServer(){
		s=new ServerTextArea(this);
		try{
			ssock = new ServerSocket(4000);
			s.write("I am online");

			while (true){
				Socket sock = ssock.accept(); 
				list.add(sock);
				s.write("Connection accepted");
				ServerThread st=new ServerThread(sock,s);
				Thread t=new Thread(st); 
				t.start();
			}
		} catch (IOException e){
			System.out.print("Could not connect.");
			System.exit(1);        
		}


	}

	public void sendToAll(String message){
		for (Socket item : list)
		{
			if(item!=null){
				BufferedWriter outForAll;
				try {
					outForAll = new BufferedWriter( new OutputStreamWriter(item.getOutputStream()));
					outForAll.write(message+"\n");
					outForAll.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public void sentToSpecificSocker(String message,int port,String host){
		for (Socket item : list)
		{
			System.out.println(item.getLocalPort()==port&&item.getInetAddress().getHostName().equals(host.trim()));
			BufferedWriter outForAll=null;
			if(item.getLocalPort()==port&&item.getInetAddress().getHostName().equals(host.trim())){
				
				try {
					outForAll = new BufferedWriter( new OutputStreamWriter(item.getOutputStream()));
					outForAll.write(message+"\n");
					outForAll.flush();
				} catch (IOException e) {
					e.printStackTrace();
				}
				try {
					outForAll.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public ServerSocket getServerSocket() {
		return ssock;
	}

}