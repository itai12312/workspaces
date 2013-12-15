package model;

import java.util.Vector;

public class Model {
	public Vector<Float> posVectRaw;
	public Vector<Float> texVectRaw;
	private Vector<Float> posVect;
	private Vector<Float> texVect;
	private boolean isTransformed;
	public double posX,posY,posZ;
	public double rotX,rotY,rotZ;
	private double scale;
	public Model() {
		zeroParams();
		posVect = new Vector<Float>();
		texVect = new Vector<Float>();
		posVectRaw = new Vector<Float>();
		texVectRaw = new Vector<Float>();
		isTransformed=false;
	}
	public Vector<Float> getPosVect() {
		if (!isTransformed) {
			transform();
			isTransformed=true;
		}
		return posVect;
	}
	public Vector<Float> getTexVect() {
		return texVect;
	}
	public void add(Model mod) {
		posVectRaw.addAll(mod.getPosVect());
		texVectRaw.addAll(mod.getTexVect());
		isTransformed=false;
	}
	public void move(double x, double y, double z) {
		posX+=x;
		posY+=y;
		posZ+=z;
		isTransformed=false;
	}
	public void rotate(double x, double y, double z) {
		rotX+=x;
		rotY+=y;
		rotZ+=z;
		isTransformed=false;
	}
	public void setAngle(double x, double y, double z) {
		rotX=x;
		rotY=y;
		rotZ=z;
		isTransformed=false;
	}
	public void setPos(double x, double y, double z) {
		posX=x;
		posY=y;
		posZ=z;
		isTransformed=false;
	}
	public void setScale(double s) {
		scale = s;
		isTransformed = false;
	}
	public void scale(double s) {
		scale *= s;
		isTransformed = false;
	}
	public void color(double r,double g,double b) {
		int i;
		texVectRaw = new Vector<Float>(posVectRaw.size());
		for (i=0;i < posVectRaw.size();i+=3) {
			texVectRaw.add((float)r);
			texVectRaw.add((float)g);
			texVectRaw.add((float)b);
		}
		isTransformed=false;
	}
	public void colorDots(double rPt, double gPt, double bPt) {
		
	}
	private void transform() {
		int i;
		double oldX,oldY,oldZ;
		double newX,newY,newZ;
		double sinX = Math.sin(rotX);
		double sinY = Math.sin(rotY);
		double sinZ = Math.sin(rotZ);
		double cosX = Math.cos(rotX);
		double cosY = Math.cos(rotY);
		double cosZ = Math.cos(rotZ);
		posVect = new Vector<Float>(posVectRaw.size());
		texVect = new Vector<Float>(texVectRaw);
		for (i=0;i < posVectRaw.size();i+=3) {
			oldX=posVectRaw.get(i)*scale;
			oldY=posVectRaw.get(i+1)*scale;
			oldZ=posVectRaw.get(i+2)*scale;
			newY=oldY*cosX-oldZ*sinX;
			newZ=oldY*sinX+oldZ*cosX;
			oldY=newY;
			oldZ=newZ;
			newX=oldX*cosY-oldZ*sinY;
			newZ=oldX*sinY+oldZ*cosY;
			
			oldX=newX;
			newX=oldX*cosZ-oldY*sinZ;
			newY=oldX*sinZ+oldY*cosZ;
			newX+=posX;
			newY+=posY;
			newZ+=posZ;
			posVect.add((float)newX);
			posVect.add((float)newY);
			posVect.add((float)newZ);
		}
	}
	public void fixTrasform() {
		posVectRaw = new Vector<Float>(this.getPosVect().size());
		texVectRaw = new Vector<Float>(this.getTexVect().size());
		posVectRaw.addAll(this.getPosVect());
		texVectRaw.addAll(this.getTexVect());
		zeroParams();
	}
	private void zeroParams() {
		posX=0;
		posY=0;
		posZ=0;
		rotX=0;
		rotY=0;
		rotZ=0;
		scale=1.0;
	}
	public static double[] frontVector(double rotX,double rotY, double rotZ) {
		double sinX = Math.sin(rotX);
		double sinY = Math.sin(rotY);
		double sinZ = Math.sin(rotZ);
		double cosX = Math.cos(rotX);
		double cosY = Math.cos(rotY);
		double cosZ = Math.cos(rotZ);
		double oldX,oldY,oldZ;
		double newX,newY,newZ;
		newY=0.0*cosX-1.0*sinX;
		newZ=0.0*sinX+1.0*cosX;
		oldY=newY;
		oldZ=newZ;
		newX=0.0*cosY+oldZ*sinY;
		newZ=0.0*sinY+oldZ*cosY;
		
		oldX=newX;
		newX=oldX*cosZ-oldY*sinZ;
		newY=oldX*sinZ+oldY*cosZ;
		double[] retval = {newX,newY,newZ};
		return retval;
	}
}
