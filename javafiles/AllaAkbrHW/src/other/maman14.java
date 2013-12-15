import java.util.Scanner;
public class maman14{
	static Scanner in=new Scanner(System.in);
	static int n,k,heapAsize,heapBsize,methodAcount=0,methodBcount=0;
	static double[] A,B;
	static int[][][] counters=new int[4][3][2];
	public static void main(String[]args){
		int []c={100,200,500,1000};
		int []d={3,50,100};
		int runningtime=100000;
		for(int l=0;l<runningtime;l++){
			for(int i=0;i<c.length;i++){
				for(int j=0;j<d.length;j++){
					setParameters(c[i], d[j]);
					//System.out.println("for n="+n+" k="+k);
					mainloopExPartB(i,j);
				}
			}
		}
		for(int i=0;i<counters.length;i++){
			for(int j=0;j<counters[i].length;j++){
				System.out.println("for n="+c[i]+" k="+d[j]+" A:"+(counters[i][j][0]/runningtime)+" B:"+(counters[i][j][1]/runningtime));
			}
		}
	}
	//sets n,k values and starts array
	private static void setParameters(int p,int o){
		n=p;
		k=o;
		A=new double[n];
		B=new double[n];
	}
	//main loop for each case of n,k with random option
	private static void mainloopExPartB(int i,int j){
		generatearray1();
		methodA();
		methodB();
		counters[i][j][0]+=methodAcount;
		counters[i][j][1]+=methodBcount;
		finish();
	}
	//this version is if user chooses random or user-input to fill array
	private static void generatearray(){
		int options=in.nextInt();
		for(int i=0;i<n;i++){
			if(options==1){
				A[i]=in.nextInt();
				B[i]=A[i];
			}else if(options==0){
				A[i]=(int)(Math.random()*1000);
				B[i]=A[i];
			}
		}
	}
	//this version for random filling array
	private static void generatearray1(){
		for(int i=0;i<n;i++){
				A[i]=(int)(Math.random()*1000);
				B[i]=A[i];
		}
	}
	//this version for users wntering n,k values
	private static void receiveparameters(){
		System.out.println("enter n \n k \n 0-for random, 1-for choosing");
		n=in.nextInt();
		k=in.nextInt();
		A=new double[n];
		B=new double[n];
	}
	//prints number of comparasions and resets counters 
	private static void finish(){
		//System.out.println("counters:A "+methodAcount+" B: "+methodBcount);
		methodAcount=0;methodBcount=0;
	}
	//method B for finding k smallest items
	private static void methodB(){
		randomizedselect(0,B.length-1,k-1);//find k smallest numbers
		quicksort(0,k-1);//orders k smallest numbers
		/*System.out.print("methodB: ");
		for(int i=0;i<k;i++){//prints numbers
			System.out.print(B[i]+" ");
		}
		System.out.println();*/
	}
	//quick sorts array
	private static void quicksort(int p,int r){
		if(p<r){
			int q=partition(p,r);
			quicksort(p,q-1);
			quicksort(q+1,r);
		}

	}
	//sorts an array into 2 sections-one with numbers greater than A[i] and one with less than A[i]
	private static double randomizedselect(int p,int r,int i){
		if(p==r)
			return A[p];
		int q=randomizedpartition(p,r);
		int o=q-p+1;
		if(i==o){
			return A[q];
		}
		else if(i<o){
			return randomizedselect(p,q-1,i);
		}else{
			return randomizedselect(q+1,r,i-o);
		}

	}
	//creates a partition based on a randomized number
	private static int randomizedpartition(int p,int r){
		int i=(int) ((Math.random()*(r-p+1))+p);
		exchange(i,r,B);
		return partition(p,r);
	}
	//creates a partition based on the last number in the array
	private static int partition(int p,int r){
		double x=B[r];
		int i=p-1;
		for(int j=p;j<r;j++){
			methodBcount++;
			if(B[j]<=x){
				i++;
				exchange(i,j,B);
			}
		}
		exchange(i+1,r,B);
		return i+1;
	}
	
	private static void methodA(){
		buildminheap();
		//System.out.print("methodA: ");
		for(int i=0;i<k;i++){
			/*System.out.print(*/helpextractmin();/*+" ");*/
		}
		//System.out.println();*/
	}
	//extracts the minimum and recreates the stack
	private static double helpextractmin(){
		if(heapAsize<1){
			System.out.println("heap underflow");
		}else{
			double min=A[0];
			A[0]=A[heapAsize-1];
			heapAsize--;
			minheapify(0);//recreate stack
			return min;
		}
		return -999;
	}
	//build the heap
	private static void buildminheap(){
		heapAsize=A.length;
		for(int i=(int)(Math.floor(heapAsize/2)-1);i>-1;i--){
			minheapify(i);	
		}
	}
	//puts i-node in order in the stack
	private static void minheapify(int i){
		int l=left(i);
		int r=right(i);
		int smallest = -1;
		if(l<heapAsize){
			methodAcount++;
			if(A[l]<A[i]){
				smallest=l;
			}
			else{
				smallest=i;
			}
		}
		if(r<heapAsize){
			methodAcount++;
			if(A[r]<A[smallest]){
				smallest=r;
			}
		}
		if(smallest!=i&&smallest>-1){
			exchange(i,smallest,A);
			minheapify(smallest);
		}

	}
	//exchanges between values t places i and j
	private static void exchange(int i,int j,double[]array){
		double k=array[i];
		array[i]=array[j];
		array[j]=k;
	}
	//returns left node
	private static int left(int i){
		return 2*i+1;
	}
	//returns right node
	private static int right(int i){
		return 2*i+2;
	}
}