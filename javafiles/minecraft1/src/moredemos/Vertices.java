package moredemos;
public class Vertices {
	double[]points;
	public Vertices(double[] points) {
			this.points = points;	
	}
	
	public void set(int location,double replacement){
		if(location<points.length){
			points[location]=replacement;
		}
	}
}