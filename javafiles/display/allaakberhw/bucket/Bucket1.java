package bucket;
import unit4.bucketLib.Bucket;
import java.util.Scanner;
public class Bucket1{
	static Scanner in=new Scanner (System.in);
	public static void main (String args[]){
		int capacity=in.nextInt();
		Bucket[]b=new Bucket[6];
		for(int i=0;i<6;i++){
			b[i]=new Bucket(capacity,""+(i+1));
			b[i].fill(capacity/(Math.pow(2,i)));
		}
	
	}
}
