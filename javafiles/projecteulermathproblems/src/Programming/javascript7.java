package Programming;
import java.io.IOException;
import java.util.Scanner;
public class javascript7 {
	static Scanner in=new Scanner(System.in);
	public static void main(String[] args) throws IOException{
		  //BufferedReader bf = new BufferedReader(new InputStreamReader(System.in));
		  System.out.println("Enter the hexa number:");
		  String str=in.next();
		 str=str.replace("0x", "");
		 str=str.replace(",", "");
		  while(!str.equals("")){
			System.out.print(inttochar(str.substring(0, 2)));
			str=str.substring(2);
		  }
		  
		  
		 
	}
	
	public static char inttochar(String str){
		//int i= Integer.parseInt(str,16);
		 // System.out.println("Decimal:="+ i);
		 // char c = (char) i;
		  return (char)Integer.parseInt(str,16);
	}
}