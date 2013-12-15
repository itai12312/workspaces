package start;

public class Triangle {
private Point firstpoint;
private Point secondpoint;
private Point secondpoint;

public Triangle(Point a,Point b,Point c){
	firstpoint= new Point(a.getX(),a.getY());
	secondpoint= new Point(b.getX(),b.getY());
	secondpoint= new Point(c.getX(),c.getY());

}
public Triangle(){
	firstpoint= new Point();
	secondpoint= new Point();
	secondpoint= new Point();
}
public void setfirstpoint(int x, int y) {
	this.firstpoint.setX(x);
	this.firstpoint.setY(y);
}

public void setsecondpoint(int x, int y) {
	this.secondpoint.setX(x);
	this.secondpoint.setY(y);
}

public void setsecondpoint(int x, int y) {
	this.secondpoint.setX(x);
	this.secondpoint.setY(y);
}

public void setfirstpoint(Point firstpoint) {
	this.firstpoint = firstpoint;
}
public Point getfirstpoint() {
	return firstpoint;
}
public void setsecondpoint(Point secondpoint) {
	this.secondpoint = secondpoint;
}
public Point getsecondpoint() {
	return secondpoint;
}
public void setsecondpoint(Point secondpoint) {
	this.secondpoint = secondpoint;
}
public Point getsecondpoint() {
	return secondpoint;
}
public String toString(){
	return "firstpoint="+firstpoint+"secondpoint="+secondpoint+"secondpoint="+secondpoint;
}
}
