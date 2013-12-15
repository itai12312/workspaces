import java.util.Scanner;


public class months {
	static Scanner in=new Scanner(System.in);
	public static void main(String[] args) {

		
		int month;
		month=in.nextInt();
		switch(month){
			case 1:System.out.println("jan");break;
			case 2:System.out.println("feb");break;
			case 3:System.out.println("march");break;
			case 4:System.out.println("april");break;
			case 5:System.out.println("may");break;
			case 6:System.out.println("june");break;
			case 7:System.out.println("july");break;
			case 8:System.out.println("august");break;
			case 9:System.out.println("september");break;
			default:System.out.println("enter a number between 1-9");break;
		
		}
	}
}