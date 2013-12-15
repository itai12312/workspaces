package model;

import java.util.Arrays;


//zero at lower left corner
public class Rectangle extends Model {
	public Rectangle(double w,double h) {
		super();
		Float[] pos = {0.0f,(float)h,0.0f,(float)w,(float)h,0.0f,0.0f,0.0f,0.0f,
				(float)w,(float)h,0.0f,0.0f,0.0f,0.0f,(float)w,0.0f,0.0f};
		posVectRaw.addAll(Arrays.asList(pos));
		this.move(0.0, 0.0, 0.0);
	}
	public Rectangle(double w,double h,double x, double y, double z) {
		super();
		Float[] pos = {0.0f,(float)h,0.0f,(float)w,(float)h,0.0f,0.0f,0.0f,0.0f,
				(float)w,(float)h,0.0f,0.0f,0.0f,0.0f,(float)w,0.0f,0.0f};
		posVectRaw.addAll(Arrays.asList(pos));
		this.move(x,y,z);
	}
	public void center() {};
}
