package moredemos;
import java.awt.Color;

import org.lwjgl.opengl.GL11;
public class Face {
	Vertices[]vertices;
	Color[]color;
	public Face(Vertices[] vertices, Color[]color) {
			this.vertices = vertices;	
			if(color.length<=vertices.length){
				this.color=color;
			}
	}
	
	public void updateVertice(int location,Vertices vertices1){
		if(location<vertices.length){
			vertices[location]=vertices1;
		}
	}
	
	public void updateColor(int location,Color color1){
		if(location<vertices.length){
			color[location]=color1;
		}
	}
	public void draw(){
		int i=0;
		for(;i<color.length;i++){
			GL11.glColor3d(color[i].getRed()/256,color[i].getGreen()/256, color[i].getBlue()/256);
			GL11.glVertex3d(vertices[i].points[0],vertices[i].points[1],vertices[i].points[2]);
		}
		for(;i<vertices.length;i++){
			GL11.glColor3d(color[color.length-1].getRed()/256,color[color.length-1].getGreen()/256, color[color.length-1].getBlue()/256);
			GL11.glVertex3d(vertices[i].points[0],vertices[i].points[1],vertices[i].points[2]);
		}
	}
}