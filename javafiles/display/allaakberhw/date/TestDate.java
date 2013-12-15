package p2;
import java.util.Scanner;
public class TestDate{
	static Scanner in=new Scanner (System.in);
	public static void main(String[] args){
		Date d1=new Date(7,6,2000);
		Date d2=new Date (1,d1.getMonth(),0001);
		System.out.println(d1.getDay());
		System.out.println(d2.getDay());
		d2.setDay(d1.getDay());
		System.out.println(d2.getDay());
		System.out.println(d1.compareTo(d2));
		Date[] date= new Date[20];
		for(int i=0;i<20;i++)
			date[i]= new Date(in.nextInt(),in.nextInt(),in.nextInt());
		boolean smaller=true;
		for (int j=0;j<19 && smaller;j++){
			if(date[j].compareTo(date[j+1])>0)
				smaller=false;
		}
		if(smaller)
		System.out.println("sorted");
		else{
			for(int k=0;k<19;k++){
				for(int l=k;l<20;l++){
					if(date[k].compareTo(date[l])==1){
						Date t=date[k];
						date[k]=date[l];
						date[l]=t;
				 		}
					}
				}
			for(int w=0;w<20;w++)	
				System.out.println(date[w]);
		}
	}


}
