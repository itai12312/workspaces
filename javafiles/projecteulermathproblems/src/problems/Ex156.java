package problems;

public class Ex156{
	public static void main(String[]args){
		long num=0;
		int d=1;
		long sum=0;
		long sumnums=0;
		while(true){
			long num1=num;
			sum+=f(num,d);
			if(sum==num1){
				sumnums+=num1;
				System.out.println("winner:***num "+sum+"****sumnum "+sumnums);
			}
			System.out.println(sum+" "+num);
			num=num1;
			num++;
		}
		
	}
	public static int f(long n,int d){
		int sum=0;
		int numdigits=numdigits(n);
		//System.out.println(n);
		while(n>0){
			//System.out.println("*"+Math.pow(10, numdigits-1));
			if(n%Math.pow(10, numdigits-1)==d){
				sum++;
			}
			n=n/10;
			numdigits--;
		}
		
		return sum;
	}
	public static int numdigits(long n){
		long e=n;
		int count=0;
		while(e>0){
			count++;
			e/=10;
		}
		
		return count;
		
	}
}
