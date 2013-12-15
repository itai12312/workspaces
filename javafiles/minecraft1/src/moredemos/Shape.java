package moredemos;

import java.awt.Color;
import java.util.Arrays;
import org.lwjgl.opengl.GL11;
public class Shape {
	float shapeAngle; // Angle of rotation for the triangles
	Trans[]trans;
	String name="";
	Face[]face;
	double translatex,translatey,translatez,rotatex,rotatey,rotatez,verticesperface;
	//GLMode? other data?
	
	public void print(){
	
		System.out.println("shapeAngle=" + shapeAngle + ", translatex=" +translatex
				+ ", translaty=" + translatey + ", translatez=" + translatez
				+ ", rotatex=" + rotatex + ", rotatey=" + rotatey
				+ ", rotatez=" + rotatez + ", faces=" + face.length
				+ ", verticesperface=" + verticesperface);
	}
	
	public void draw(){
		for(int i=0;i<face.length;i++){
			face[i].draw();
		}
	}
	
	public void updateFace(int location,Face face1){
		if(location<face.length){
			face[location]=face1;
		}
	}

	public Shape(float shapeAngle, moredemos.Trans[]trans, String name,
			Face[] face) {
		this.shapeAngle = shapeAngle;
		this.trans = trans;
		this.name = name;
		this.face = face;
	}
	
	public Shape(float shapeAngle, moredemos.Trans[]trans, String name,
			double[][][]coordinates,double[][][]color) {
		this.shapeAngle = shapeAngle;
		this.trans = trans;
		this.name = name;
		this.face = face;
	}
	
	
}