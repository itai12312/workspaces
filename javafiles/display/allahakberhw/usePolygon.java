package allahakberhw;
import p1.Point;
public class usePolygon {

	public static void main(String[] args) {
		Point p[]=new Point[3];
		for (int i=0;i<3;i++)
			p[i]=new Point(Math.pow(i,2),i*2);
		Polygon poly=new Polygon(3, p);
		System.out.println(poly);
		System.out.println(poly.getNumVertexes());
		System.out.println(poly.getPerimeter());
		System.out.println(poly.getSmallestSideLength());
	}
}

