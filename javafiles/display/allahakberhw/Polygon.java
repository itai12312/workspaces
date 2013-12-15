package allahakberhw;
import p1.Point;
public class Polygon
{
	static final int N=20;
	private int numVertexes;
	private Point point[]=new Point[N];
	
	public Polygon(int numVertexes, Point point[]){
		this.numVertexes=numVertexes;
		for (int i=0;i<numVertexes;i++)
			this.point[i]=new Point(point[i]);
	}
		
	public int getNumVertexes(){return numVertexes;}
	public double getSideLength(int i)
	{if (i!=numVertexes-1)return Math.sqrt((point[i+1].getX()-point[i].getX())*(point[i+1].getX()-point[i].getX())+(point[i+1].getY()-point[i].getY())*(point[i+1].getY()-point[i].getY()));
	return Math.sqrt((point[0].getX()-point[i].getX())*(point[0].getX()-point[i].getX())+(point[0].getY()-point[i].getY())*(point[0].getY()-point[i].getY()));
	}
	
	public double getPerimeter()
	{
		double perimeter=0;
		for (int i=0;i<numVertexes;i++)
			perimeter+=this.getSideLength(i);
		return perimeter;
	}
	public double getSmallestSideLength()
	{
		double min=getSideLength(0);
		for (int i=1;i<numVertexes;i++)
			if (min>getSideLength(i)) min=getSideLength(i);
		return min;
	}
	
	public void addPoint(Point point)
	{
		this.point[numVertexes]=point;
		numVertexes++;
	}
	
	public String toString()
	{
		String s="numVertexes:"+numVertexes+" {(x,y)|"+point[0].toString2();
		for (int i=1;i<numVertexes;i++)s+=","+point[i].toString2();
		return s+"}";
	}
}
