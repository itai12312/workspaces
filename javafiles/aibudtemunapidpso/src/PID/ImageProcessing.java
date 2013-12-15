package SmartestCamera;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Scanner;
public class ImageProcessing {
	static Scanner in=new Scanner(System.in);
	boolean [][]binaryimage;
	int[][]counter;
	static int length=50;
	static int height=50;
	public static void main(String[] args) {
		length=in.nextInt();
		height=in.nextInt();		
	}
	
	public ImageProcessing(int length,int height){
		this.length=length;
		this.height=height;
		counter=booleantocounter(binaryimage);
	}
	
	public int[][] booleantocounter(boolean[][]thi){
		int [][]counter=new int[this.length][thi[0].length];
		for(int i=0;i<thi.length;i++){
			for(int j=0;j<thi[i].length;j++){
				int delta=0,x=0,y=0;
				if(binaryimage[i][j])
					delta=1;
				else 
					delta=0;
				if(i!=0){
					x=counter[i][j];
				}
				if(j!=0){
					y=counter[i][j];
				}
				counter[i][j]=delta+x+y-1;
			}
		}
		return counter;
	}
	
	public int count(int[][]counter,int x,int y,int length,int height){
		return counter[x][y]+counter[x+length][y+height]-counter[x+length][y]-counter[x][y+height];
	}
	
	public ArrayList<Rect> findImage1(boolean[][]whattofind,boolean[][]where,int tolerance){
		ArrayList<Rect>potenialimages=new ArrayList<Rect>();
		int[][]count=booleantocounter(whattofind);
		int[][]whereto=booleantocounter(where);
		int target=count(count,0,0,whattofind.length,whattofind[0].length);
		for (int i = 0; i < whereto.length; i++) {
			for (int j = 0; j < whereto.length; j++) {
				int y=count(count,i,j,whereto.length,whereto[0].length);
				if(Math.abs(y-target)<tolerance){
					potenialimages.add(new Rect(i,j,whereto.length,whereto[0].length));
				}
			}
		}
		return potenialimages;
	}
	
	public void evenmoretolerance(int[][]where,int[][]whattofind,ArrayList<Rect>potentislPoints,int tolerance){
		int target=count(whattofind, 0, 0, whattofind.length, whattofind[0].length);
		for(int i=0;i<potentislPoints.size();i++){
			Rect rect=potentislPoints.get(i);
			if(Math.abs(count(where,rect.getX(),rect.getY(),rect.getLength()/2,rect.getHeight()/2)-target)>tolerance){
				potentislPoints.remove(i);
			}
			else if(Math.abs(count(where,rect.getX(),rect.getY(),rect.getLength()/2,rect.getHeight()/2)-target)>tolerance){
				
			}
		}
		
	}
	
	class Rect{
		int x=0,y=0,length=0,height=0;
		public Rect(int x,int y,int length,int height) {
			this.x=x;
			this.y=y;
			this.length=length;
			this.height=height;
		}
		public int getX() {
			return x;
		}
		public void setX(int x) {
			this.x = x;
		}
		public int getY() {
			return y;
		}
		public void setY(int y) {
			this.y = y;
		}
		public int getLength() {
			return length;
		}
		public void setLength(int length) {
			this.length = length;
		}
		public int getHeight() {
			return height;
		}
		public void setHeight(int height) {
			this.height = height;
		}
		
	}
}