import java.util.Scanner;
public class Bucket2 {
	
public static void main (String args[]){
	Scanner in= new Scanner(System.in);
	int capacity=in.nextInt();
	int size=capacity;
	Bucket[]arr= new Bucket[6];
	for(int i=0;i<6;i++){
		arr[i]= new Bucket(capacity,size);
		size/=2;
	}
	
}}
