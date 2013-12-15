import java.util.Scanner;
public class bucket3 {
	
public static void main (String args[]){
	static Scanner in= new Scanner (System.in);
	int size=in.nextInt();
	int a,capacity;
	a=capacity=in.nextInt();	
	Bucket[]arr= new Bucket[size];
	for(int i=0;i<6;i++){
		arr[i]= new Bucket(20,20*Math.random() );
	}
	double max=arr[0].getCurrentAmount();
	for(int k=1;k<6;k++){
		if(arr[k]>max)
			max=arr[k];
	}
	for(int k=0;k<6;k++){
	Bucket w= new Bucket(max-arr[k].getCurrentAmount(),max-arr[k].getCurrentAmount());
	arr[k].pourInto(w);
	}
	
}}
