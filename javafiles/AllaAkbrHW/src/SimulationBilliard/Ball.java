package SimulationBilliard;

import java.awt.Color;

public class Ball {
	int r,g,b;
	Color c;
	double x,y,vx,vy,ax,ay;
	double friction;
	double mass;
	double radius;
	public Ball(){
		
	}
	public void positive(String s){
		
	}
	public void negative(String s){
		if(s.equals("x"))
			x=-1*Math.abs(x);
		else if(s.equals("y"))
			y=-1*Math.abs(y);
		else if(s.equals("y"))
			y=-1*Math.abs(y);
		else if(s.equals("vy"))
			vy=-1*Math.abs(vy);
		else if(s.equals("vx"))
			vx=-1*Math.abs(vx);
		else if(s.equals("ax"))
			ax=-1*Math.abs(ax);
		else if(s.equals("ay"))
			ay=-1*Math.abs(ay);
		}
	public void reverse(String s){
		
	}
	public double getRadius() {
		return radius;
	}
	public void setRadius(double radius) {
		this.radius = radius;
	}
	public int getR() {
		return r;
	}
	public void setR(int r) {
		this.r = r;
	}
	public int getG() {
		return g;
	}
	public void setG(int g) {
		this.g = g;
	}
	public int getB() {
		return b;
	}
	public void setB(int b) {
		this.b = b;
	}
	public Color getC() {
		return c;
	}
	public void setC(Color c) {
		this.c = c;
	}
	public double getX() {
		return x;
	}
	public void setX(double x) {
		this.x = x;
	}
	public double getY() {
		return y;
	}
	public void setY(double y) {
		this.y = y;
	}
	public double getVx() {
		return vx;
	}
	public void setVx(double vx) {
		this.vx = vx;
	}
	public double getVy() {
		return vy;
	}
	public void setVy(double vy) {
		this.vy = vy;
	}
	public double getAx() {
		return ax;
	}
	public void setAx(double ax) {
		this.ax = ax;
	}
	public double getAy() {
		return ay;
	}
	public void setAy(double ay) {
		this.ay = ay;
	}
	public double getFriction() {
		return friction;
	}
	public void setFriction(double friction) {
		this.friction = friction;
	}
	public double getMass() {
		return mass;
	}
	public void setMass(double mass) {
		this.mass = mass;
	}
	
}
