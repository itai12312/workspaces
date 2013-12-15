package model;

import java.util.Arrays;
import java.util.Random;

public class SeaFloor extends Model{
	public SeaFloor() {
		super();
		int x,y;
		int squareIdx = 0;
		double[][] height = new double[11][11];
		Random rand = new Random();
		Float[] verts = new Float[100*6*3];
		for (y=0;y < 11;y++)
			for (x=0;x < 11;x++)
				height[y][x] = rand.nextDouble()/8.0;
		for (y=0;y < 10;y++)
			for (x=0;x < 10;x++) {
				verts[squareIdx] = (float)(x/10.0); 
				verts[squareIdx+1] = (float)height[y][x]; 
				verts[squareIdx+2] = (float)(y/10.0); 
				verts[squareIdx+3] = (float)(x/10.0); 
				verts[squareIdx+4] = (float)height[y+1][x]; 
				verts[squareIdx+5] = (float)((y+1)/10.0); 
				verts[squareIdx+6] = (float)((x+1)/10.0); 
				verts[squareIdx+7] = (float)height[y][x+1]; 
				verts[squareIdx+8] = (float)(y/10.0); 
				verts[squareIdx+9] = (float)(x/10.0); 
				verts[squareIdx+10] = (float)height[y+1][x]; 
				verts[squareIdx+11] = (float)((y+1)/10.0); 
				verts[squareIdx+12] = (float)((x+1)/10.0); 
				verts[squareIdx+13] = (float)height[y][x+1]; 
				verts[squareIdx+14] = (float)(y/10.0); 
				verts[squareIdx+15] = (float)((x+1)/10.0); 
				verts[squareIdx+16] = (float)height[y+1][x+1]; 
				verts[squareIdx+17] = (float)((y+1)/10.0); 
				squareIdx+=18;
			}
		posVectRaw.addAll(Arrays.asList(verts));
		this.color(0.0, 0.0, 0.0);
		this.move(0.0, 0.0, 0.0);
	}
}
