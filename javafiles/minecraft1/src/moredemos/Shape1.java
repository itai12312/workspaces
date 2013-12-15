package moredemos;

import org.lwjgl.opengl.GL11;

public class Shape1 {
	double shapeAngle; // Angle of rotation for the triangles
	Trans[]trans;
	String name="";
	double[][][]coordinates;//coordinates[i]=vertices
	double[][][]color;
	//GLMode? other data?

	public void print(){
		System.out.println(name+": shapeAngle=" + shapeAngle + ", translatex=" + trans[0].trans[0]
				+ ", translaty=" + trans[0].trans[1] + ", translatez=" + trans[0].trans[2]
				+ ", rotatex=" + trans[1].trans[0] + ", rotatey=" + trans[1].trans[1]
				+ ", rotatez=" + trans[1].trans[2] );
	}
	
	public void draw(){
		if(trans[0].type.equals("translate")&&trans[1].type.equals("rotate")){
			GL11.glTranslated(trans[0].trans[0], trans[0].trans[1], trans[0].trans[2]); // Move Right And Into The Screen
			GL11.glRotated(shapeAngle,trans[1].trans[0], trans[1].trans[1], trans[1].trans[2]);
		}else{
			GL11.glTranslated(trans[1].trans[0], trans[1].trans[1], trans[1].trans[2]); // Move Right And Into The Screen
			GL11.glRotated(shapeAngle,trans[0].trans[0], trans[0].trans[1], trans[0].trans[2]);		 // Rotate The Cube On X, Y & Z
		}
		
		if(this.name.equals("quad")){
			GL11.glBegin(GL11.GL_QUADS); // Start Drawing The Cube
		}else if(this.name.equals("triangle")){
			GL11.glBegin(GL11.GL_TRIANGLES);
		}
		//System.out.println("Shape GLBegin");
		for(int i=0;i<coordinates.length;i++){
			int j=0;
			for(;j<color[i].length;j++){
				GL11.glColor3d(color[i][j][0],color[i][j][1], color[i][j][2]);
				GL11.glVertex3d(coordinates[i][j][0],coordinates[i][j][1],coordinates[i][j][2]);
			}
			for(;j<coordinates[i].length;j++){
				//System.out.println("shape1 vcolor:"+color[i][color[i].length-1][0]+" "+color[i][color[i].length-1][1]+" "+ color[i][color[i].length-1][2]);
				GL11.glColor3d(color[i][color[i].length-1][0],color[i][color[i].length-1][1], color[i][color[i].length-1][2]);
				//System.out.println("shape1 vcoords:"+coordinates[i][j][0]+" "+coordinates[i][j][1]+" "+coordinates[i][j][2]);
				GL11.glVertex3d(coordinates[i][j][0],coordinates[i][j][1],coordinates[i][j][2]);
			}
		}
		GL11.glEnd();
	}

	public Shape1(double shapeAngle, Trans[] trans, String name,
			double[][][] coordinates, double[][][] colorpoints) {
		this.shapeAngle = shapeAngle;
		this.trans = trans;
		this.name = name;
		this.coordinates = coordinates;
		this.color = colorpoints;
	}
	
	
}