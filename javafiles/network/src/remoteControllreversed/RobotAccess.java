package remoteControllreversed;
import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.InputEvent;
public class RobotAccess {
	static Robot robot=null;
	public static void dealWith(String serverString){
		try {
			robot=new Robot();
		} catch (AWTException e) {
			e.printStackTrace();
		}

		if(serverString.indexOf(",")!=(-1)&&serverString.indexOf(",",serverString.indexOf(",")+1)!=-1){
			int k=serverString.indexOf(",");
			int x=Integer.parseInt(serverString.substring(0,k));
			int y=Integer.parseInt(serverString.substring(k+1,serverString.indexOf(",",k+1)));
			String command=serverString.substring(serverString.indexOf(",",k+1)+1,serverString.length()-1);
			//System.out.println(stringServer);
			int button=Integer.parseInt(serverString.substring(serverString.length()-1, serverString.length()));
			System.out.println(serverString+":"+x+" "+y+" "+command+" "+button);
			button=InputEvent.BUTTON1_DOWN_MASK;
			if(command.equals("clicked")){
					 robot.mouseMove(x, y);
					 robot.mousePress(button);
					 robot.mouseRelease(button);
			}else if(command.equals("pressed")){
					 robot.mouseMove(x, y);
					 robot.mousePress(button);
			}else if(command.equals("relesed")){
					robot.mouseMove(x, y);
					robot.mouseRelease(button);
			}else if(command.equals("dragged")){
					 robot.mousePress(button);
					 robot.mouseMove(x, y);
					 robot.mousePress(button);
			}else if(command.equals("moveded")){
					robot.mouseMove(x, y);
			}else if(command.equals("whemmed")){
					 robot.mouseWheel(x);
			}
		}else if(serverString.indexOf(",")!=-1){
			int keycode=Integer.parseInt(serverString.substring(0,serverString.indexOf(",")));
			String command=serverString.substring(serverString.indexOf(",")+1,serverString.length());
			System.out.println(serverString+":"+keycode+" "+command);
			if(command.equals("press")){
					 robot.keyPress(keycode);
			}else if(command.equals("typed")){
					robot.keyPress(keycode);
					robot.keyRelease(keycode);
			}else if(command.equals("reles")){
					robot.keyRelease(keycode);
			}
		}else{
			//System.out.println(serverString);
			//writeWord(serverString);
		}
	}


	public static void main(String[]args){
	}
}
