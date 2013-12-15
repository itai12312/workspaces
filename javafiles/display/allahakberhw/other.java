package allahakberhw;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Timer;
import java.util.TimerTask;
public class other {
	static Runtime r=Runtime.getRuntime();
	static String[]s={"explorer.exe","iexplore.exe","eclipse.exe"};
	static Process c = null;
	public static void main(String[] args) throws InterruptedException {
		
		TimerTask a=new TimerTask(){
			public void run(){check();};
		};
		Timer t=new Timer();
		long delay = 0;
		long period = 0;
		t.scheduleAtFixedRate(a, delay, period);
	
		
		
			
		}
	public static void check(){
		try {  
			String line; 
			Process p = Runtime.getRuntime().exec(System.getenv("windir") +"\\system32\\"+"tasklist.exe"); 
			BufferedReader input =new BufferedReader(new InputStreamReader(p.getInputStream()));
			while ((line = input.readLine()) != null) {
				for(int i=0;i<s.length;i++){
					if(line.contains(s[i])){
						c=r.exec("H://"+s[i]+".bat");
						c.waitFor();
						c.destroy();
					}
					
				}
				System.out.println(line); //<-- Parse data here.  
				} 
			input.close();   
			} catch (Exception err) {  
				err.printStackTrace(); 
			}
		
		
	}
		
	}


