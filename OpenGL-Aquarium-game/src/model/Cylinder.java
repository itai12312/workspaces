package model;

import java.util.Arrays;

public class Cylinder extends Model {
	public Cylinder(double rad,double h) {
		super();
		double ang;
		double inc = Math.PI/8;
		int face = 0;
		Float[] pos = new Float[16*4*3*3];
		Float[] tex = new Float[16*4*3*2];
		for (ang = 0;ang < Math.PI*2-0.001;ang+=inc) {
			pos[face*36] = (float)(rad*Math.cos(ang));  //lower disc
			pos[face*36+1] = 0.0f;
			pos[face*36+2] = (float)(rad*Math.sin(ang));
			pos[face*36+3] = (float)(rad*Math.cos(ang+inc));
			pos[face*36+4] = 0.0f;
			pos[face*36+5] = (float)(rad*Math.sin(ang+inc));
			pos[face*36+6] = 0.0f;
			pos[face*36+7] = 0.0f;
			pos[face*36+8] = 0.0f;
			
			pos[face*36+9] = (float)(rad*Math.cos(ang));  //side - upper trig
			pos[face*36+10] = (float)h;
			pos[face*36+11] = (float)(rad*Math.sin(ang));
			pos[face*36+12] = (float)(rad*Math.cos(ang+inc));
			pos[face*36+13] = (float)h;
			pos[face*36+14] = (float)(rad*Math.sin(ang+inc));
			pos[face*36+15] = (float)(rad*Math.cos(ang));
			pos[face*36+16] = 0.0f;
			pos[face*36+17] = (float)(rad*Math.sin(ang));
			
			pos[face*36+18] = (float)(rad*Math.cos(ang+inc));   //side - lower trig 
			pos[face*36+19] = (float)h;
			pos[face*36+20] = (float)(rad*Math.sin(ang+inc));
			pos[face*36+21] = (float)(rad*Math.cos(ang));
			pos[face*36+22] = 0.0f;
			pos[face*36+23] = (float)(rad*Math.sin(ang));
			pos[face*36+24] = (float)(rad*Math.cos(ang+inc));
			pos[face*36+25] = 0.0f;
			pos[face*36+26] = (float)(rad*Math.sin(ang+inc));
			
			pos[face*36+27] = (float)(rad*Math.cos(ang));  //upper disc
			pos[face*36+28] = (float)h;
			pos[face*36+29] = (float)(rad*Math.sin(ang));
			pos[face*36+30] = (float)(rad*Math.cos(ang+inc));
			pos[face*36+31] = (float)h;
			pos[face*36+32] = (float)(rad*Math.sin(ang+inc));
			pos[face*36+33] = 0.0f;
			pos[face*36+34] = (float)h;
			pos[face*36+35] = 0.0f;
			face++;
		}
		face = 0;
		for (ang = 0;ang < Math.PI*2-0.001;ang+=inc) {
			tex[face*24] = (float)(ang/(Math.PI*2));    //upper trig
			tex[face*24+1] = 0.9f;
			tex[face*24+2] = (float)((ang+inc)/(Math.PI*2));
			tex[face*24+3] = 0.9f;
			tex[face*24+4] = 0.0f;
			tex[face*24+5] = 1.0f;
			
			tex[face*24+6] = (float)(ang/(Math.PI*2));    //upper side trig
			tex[face*24+7] = 0.9f;
			tex[face*24+8] = (float)((ang+inc)/(Math.PI*2));
			tex[face*24+9] = 0.9f;
			tex[face*24+10] = (float)(ang/(Math.PI*2));   
			tex[face*24+11] = 0.1f;
			tex[face*24+12] = (float)((ang+inc)/(Math.PI*2));
			tex[face*24+13] = 0.9f;
			tex[face*24+14] = (float)(ang/(Math.PI*2));    //lower side trig
			tex[face*24+15] = 0.1f;
			tex[face*24+16] = (float)((ang+inc)/(Math.PI*2));
			tex[face*24+17] = 0.1f;
			
			tex[face*24+18] = (float)(ang/(Math.PI*2));    //lower trig
			tex[face*24+19] = 0.1f;
			tex[face*24+20] = (float)((ang+inc)/(Math.PI*2));
			tex[face*24+21] = 0.1f;
			tex[face*24+22] = 0.0f;
			tex[face*24+23] = 0.0f;
			face++;
		}
		posVectRaw.addAll(Arrays.asList(pos));
		texVectRaw.addAll(Arrays.asList(tex));
		this.move(0.0, 0.0, 0.0);
	}

}
