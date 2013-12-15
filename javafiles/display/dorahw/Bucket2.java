import java.util.Scanner;
public class Bucket2 {
	
public static void main (String args[]){
	static Scanner in= new Scanner (System.in);
	int capacity=in.nextInt();
	int size=capacity;
	Bucket[]arr= new Bucket[6];
	for(int i=0;i<6;i++){
		arr[i]= new Bucket(capcity,size);
		size/=2;
	}
	
}}
