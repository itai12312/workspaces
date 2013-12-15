package allahakberhw;
import java.awt.Point;
import java.awt.geom.Point2D;
public class assigment2 extends java.awt.Point{
	private double x,y;
	private double[] lineartransformation;
	private double[] matrixmeyatzeget;
	private double[] bases1;
	private double[] bases2;
	private double[] matrixconverstionfrombases1tobases2;
	public static void main(String[] args) {

		
	}
	
	public void lineartransforamtion(){
		double[]sums=new double[lineartransformation.length];
		for(int i=0;i<lineartransformation.length;i++){
			//sums[i]=lineartransformation[i][1]*x+lineartransformation[i][1]*y;		
		}
	}
	
	public assigment2(int x,int y){
		this.x=x;
		this.y=y;
	}
	
	public assigment2(int x,String type){
		this(x,0);
	}
	public assigment2(int y){
		this(0,y);
	}
	public double[] symmetry(){
		double[] a=new double[2];
		a[0]=this.x;a[1]=-this.y;
		return a;		
	}
	
	public String location(){		
		if(this.x==0){
			return "on y-axis";
		}
		if(this.y==0){
			return "on x-axis";
		}
		if(this.x>0&&this.y>0){return "on first quadrant";}
		if(this.x<0&&this.y>0){return "on second quadrant";}
		if(this.x<0&&this.y<0){return "on third quadrant";}
		if(this.x>0&&this.y<0){return "on fourth quadrant";}
		return "";
	}

	@Override
	public boolean equals(Object obj) {
		// TODO Auto-generated method stub
		return super.equals(obj);
	}


	@Override
	public Point getLocation() {
		// TODO Auto-generated method stub
		return super.getLocation();
	}


	@Override
	public double getX() {
		// TODO Auto-generated method stub
		return super.getX();
	}


	@Override
	public double getY() {
		// TODO Auto-generated method stub
		return super.getY();
	}


	@Override
	public void move(int x, int y) {
		// TODO Auto-generated method stub
		super.move(x, y);
	}


	@Override
	public void setLocation(double x, double y) {
		// TODO Auto-generated method stub
		super.setLocation(x, y);
	}


	@Override
	public void setLocation(int x, int y) {
		// TODO Auto-generated method stub
		super.setLocation(x, y);
	}


	@Override
	public void setLocation(Point p) {
		// TODO Auto-generated method stub
		super.setLocation(p);
	}


	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return super.toString();
	}


	@Override
	public void translate(int dx, int dy) {
		// TODO Auto-generated method stub
		super.translate(dx, dy);
	}


	@Override
	public Object clone() {
		// TODO Auto-generated method stub
		return super.clone();
	}


	@Override
	public double distance(double px, double py) {
		// TODO Auto-generated method stub
		return super.distance(px, py);
	}


	@Override
	public double distance(Point2D pt) {
		// TODO Auto-generated method stub
		return super.distance(pt);
	}


	@Override
	public double distanceSq(double px, double py) {
		// TODO Auto-generated method stub
		return super.distanceSq(px, py);
	}


	@Override
	public double distanceSq(Point2D pt) {
		// TODO Auto-generated method stub
		return super.distanceSq(pt);
	}


	@Override
	public void setLocation(Point2D p) {
		// TODO Auto-generated method stub
		super.setLocation(p);
	}

}
