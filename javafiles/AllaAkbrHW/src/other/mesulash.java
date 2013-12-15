import java.util.Scanner;
public class mesulash {
	static Scanner in=new Scanner(System.in);
	public static void main(String[] args) {
		int num,mone = 0,tzover=0;
		for (num=10; num<10000;num++){
			int sum=0;
			int num1=num;
			while(num>0){
				int dig=num%10;
				sum+=Math.pow(dig, 3);
				num=num/10;
			}
			num=num1;
			if(num==sum){
				System.out.println(num);
				mone++;
				tzover+=num;
			}
		}
		System.out.println(mone);
		if(mone>0){
			System.out.println(tzover/mone);
		}
	}
}
