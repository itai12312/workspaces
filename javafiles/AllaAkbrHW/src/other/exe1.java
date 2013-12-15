import java.util.Scanner;
public class exe1 {
	static Scanner in=new Scanner(System.in);
	public static void main(String[] args) {

		int num,dig1,dig2,dig3;
		num=in.nextInt();
		if(num<1000 && num>99){
			dig1=num%10;
			dig3=num/100;
			dig2=(num%100)/10;
			System.out.println(dig2+dig1+dig3);
			System.out.println((dig2+dig1+dig3)%3==0);
		}else{
			System.out.println("enter 3 digit number");
		}
	}
}