package bucket;
import unit4.bucketLib.Bucket;
import java.util.Scanner;
public class Bucket2{
	static Scanner in= new Scanner (System.in);
	public static void main (String args[]){
		int n=in.nextInt();
		Bucket[]b= new Bucket[n];
		double max=0;
		for(int i=0;i<n;i++){
			b[i]=new Bucket(20,""+(i+1));
			b[i].fill(Math.random()*20);
			max=Math.max(b[i].getCurrentAmount(), max);
		}
		for(int j=0;j<n;j++){
			b[j].fill(max-b[j].getCurrentAmount());
		}
	}
}
