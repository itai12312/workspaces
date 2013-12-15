package model;

import objread.GL_Triangle;

public class TriangleMesh extends Model {
	public TriangleMesh(GL_Triangle[] trigs) {
		super();
		int i;
		for (i=0;i < trigs.length;i++) {
			posVectRaw.add(trigs[i].p1.pos.x);
			posVectRaw.add(trigs[i].p1.pos.y);
			posVectRaw.add(trigs[i].p1.pos.z);
			posVectRaw.add(trigs[i].p2.pos.x);
			posVectRaw.add(trigs[i].p2.pos.y);
			posVectRaw.add(trigs[i].p2.pos.z);
			posVectRaw.add(trigs[i].p3.pos.x);
			posVectRaw.add(trigs[i].p3.pos.y);
			posVectRaw.add(trigs[i].p3.pos.z);
			texVectRaw.add(trigs[i].uvw1.x);
			texVectRaw.add(trigs[i].uvw1.y);
			texVectRaw.add(trigs[i].uvw2.x);
			texVectRaw.add(trigs[i].uvw2.y);
			texVectRaw.add(trigs[i].uvw3.x);
			texVectRaw.add(trigs[i].uvw3.y);
		}
		this.move(0.0, 0.0, 0.0);
	}
	public TriangleMesh(GL_Triangle[] trigs,double r, double g, double b) {
		super();
		int i;
		for (i=0;i < trigs.length;i++) {
			posVectRaw.add(trigs[i].p1.pos.x);
			posVectRaw.add(trigs[i].p1.pos.y);
			posVectRaw.add(trigs[i].p1.pos.z);
			posVectRaw.add(trigs[i].p2.pos.x);
			posVectRaw.add(trigs[i].p2.pos.y);
			posVectRaw.add(trigs[i].p2.pos.z);
			posVectRaw.add(trigs[i].p3.pos.x);
			posVectRaw.add(trigs[i].p3.pos.y);
			posVectRaw.add(trigs[i].p3.pos.z);
		}
		this.color(r, g, b);
		this.move(0.0, 0.0, 0.0);
	}
}
