package Dora;
public class Exercises {
	public static void main(String[] args) {
		//exe16(1);
		int []a={1,2,3,4};
		System.out.println(exe12(a));	
	}
	public static boolean exe12(int[]a){
		if(a.length==0){
			return true;
		}
		else if(a[0]==a[0+a.length/2]){
			return exe12(trim(a)); 
		}
		else{
			return false;
		}
	}	
	public static int[] trim(int []a){
	int[]b=new int[a.length-2];	
	int j=0;
	for(int i=0;i<a.length;i++){
		if(i!=0&&i!=a.length/2){
			b[j]=a[i];
			j++;
		}
	}
	return b;	
	}
	static int d=2;
	public static void exe16(int n){
		System.out.print(n+" ");
		exe16(n+d);
	}
	
	public static void exe14(int n){
		System.out.print(n%10+" ");
		exe14(n/10);
	}	
}