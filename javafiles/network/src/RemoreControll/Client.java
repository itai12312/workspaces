package RemoreControll;
import java.awt.AWTException;
import java.awt.Robot;
import java.io.*;
import java.net.*;

//get image-like vnc
//send message from server to specific client only allowed as a response- maybe start send messages in separte thread from recieving them???
class NetworkRead implements Runnable{
	private Socket socket;
	ClientTextArea tx;
	Robot robot=null;
	public NetworkRead(Socket socket,ClientTextArea tx){
		this.socket=socket;
		this.tx=tx;
		try {
			robot=new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}
	public void run(){
		try{

			BufferedReader in= new BufferedReader( new InputStreamReader(socket.getInputStream()));
			String serverString;
			while ((serverString=in.readLine())!=null) {
				tx.write("message recieved from"+socket.getInetAddress()+":"+serverString+"\n");          //Server says...
				if(serverString.indexOf(",")!=(-1)&&serverString.indexOf(",",serverString.indexOf(",")+1)!=-1){
					int k=serverString.indexOf(",");
					int x=Integer.parseInt(serverString.substring(0,k));
					int y=Integer.parseInt(serverString.substring(k,serverString.indexOf(",",k+1)));
					String command=serverString.substring(serverString.indexOf(",",k+1)+1,serverString.indexOf(",",k+1)+7);
					int button=Integer.parseInt(serverString.substring(serverString.indexOf(",",k+1)+7, serverString.length()));
					if(command.equals("clicked")){
						// robot.mouseMove(x, y);
						//	 robot.mousePress(button);
						//	 robot.mouseRelease(button);
					}else if(command.equals("pressed")){
						// robot.mouseMove(x, y);
						// robot.mousePress(button);
					}else if(command.equals("relesed")){
						//robot.mouseMove(x, y);
						//robot.mouseRelease(button);
					}else if(command.equals("dragg")){
						// robot.mousePress(button);
						// robot.mouseMove(x, y);
						// robot.mousePress(button);
					}else if(command.equals("moved")){
						//robot.mouseMove(x, y);
					}else if(command.equals("whemm")){
						// robot.mouseWheel(x);
					}
				}else{
					if(serverString.indexOf(",")!=-1){
						int keycode=Integer.parseInt(serverString.substring(0,serverString.indexOf(",")));
						String command=serverString.substring(serverString.indexOf(",")+1,serverString.length());
						if(command.equals("press")){
							// robot.keyPress(keycode);
						}else if(command.equals("typed")){
							//robot.keyPress(keycode);
							//robot.keyRelease(keycode);
						}else if(command.equals("reles")){
							//robot.keyRelease(keycode);
						}
					}
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
		tx.write("Disconnected from server"+"\n");
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
}