package dorahw;
import java.util.*;
public class TestDate {
	static Scanner in= new Scanner (System.in);
	public static void main(String[] args) {
		Date first=new Date(3,4,1000);
		Date second= new Date (7,8,2134);
		System.out.println(first.getDay());
		System.out.println(second.getMonth());
		System.out.println(second.getYear());
		first.setDay(7);
		first.setMonth(8);
		first.setYear(2134);
		System.out.println(first.compareTo(second));
		System.out.println(first);	
		int size=3;
		Date[] arr= new Date[size];
		for(int x =0; x<size;x++){
			int day= in.nextInt();
			int mounth= in.nextInt();
			int year= in.nextInt();
			arr[x]= new Date(day,mounth,year);
			
		}
		boolean a1= true;
		for (int y=0;y<size-1;y++){
			if(arr[y].compareTo(arr[y+1])>0)
				a1=false;
		}
		if(a1)
			System.out.println("fine");
		else{
			int z;
			for(z=0;z<size-1;z++){
				for( int z2=z;z2<size;z2++){
				 if(arr[z].compareTo(arr[z2])>0){
					 Date temp=arr[z];
					 arr[z]=arr[z2];
					 arr[z2]=temp;
				 }
				 }
			
			}
			for(int w=0;w<size;w++)	
				System.out.println(arr[w]);
		}
	}


}
