import java.util.Scanner;
public class targil15amud108{
	static Scanner in=new Scanner(System.in);
	public static void main(String[] args) {
		int num,dig1,dig2,dig3;
		num=in.nextInt();
		if(num<1000 && num>99){
			dig1=num%10;
			dig3=num/100;
			dig2=(num%100)/10;
			
			if(num==(Math.pow(dig1,3)+Math.pow(dig2,3)+Math.pow(dig3,3))){
				System.out.println("triangle");
			}
		
		}else{
			System.out.println("enter 3 digit number");
		}
	}
}