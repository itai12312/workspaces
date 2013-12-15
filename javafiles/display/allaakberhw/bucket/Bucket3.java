package bucket;
import unit4.bucketLib.Bucket;
import java.util.Scanner;
public class Bucket3{
	static Scanner in= new Scanner (System.in);
	public static void main (String args[]){
		int n=in.nextInt();
		Bucket[]b= new Bucket[n];
		for(int i=0;i<n;i++){
			b[i]= new Bucket((int)(Math.random()*10),""+(i+1));
			b[i].fill(Math.random()*b[i].getCapacity());
		}
		for(int i=0;i<n;i++){
			for(int j=i;j<n;j++){
				if(b[i].getCurrentAmount()>b[j].getCurrentAmount()){
					Bucket t=b[i];
					b[i]=b[j];
					b[j]=t;
				}
			}
		}
		for(int t=0;t<n;t++){
			System.out.println(b[t].getCurrentAmount());
		}
	
}}
