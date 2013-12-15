import java.util.Scanner;
public class bucket4 {
	
public static void main (String args[]){
	static Scanner in= new Scanner (System.in);
	int size=in.nextInt();
	int capacity=in.nextInt();
	int a=capacity;
	Bucket[]arr= new Bucket[size];
	for(int i=0;i<size;i++){
		arr[i]= new Bucket(10*Math.random(),10*Math.random() );
	}

	for(int z=0;z<size;z++){
		for( int z2=z;z2<size;z2++){
		 if(arr[z].getCurrentAmount()>arr[z2].getCurrentAmount()){
			 Bucket temp=arr[z];
			 arr[z]=arr[z2];
			 arr[z2]=temp;
		 }
		 
	}}
		for(int t=0;t<size;t++){
			System.out.println(arr[t].getCurrentAmount());
		}
	
}}
