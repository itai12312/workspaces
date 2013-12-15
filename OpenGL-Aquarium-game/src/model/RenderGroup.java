package model;

import java.nio.FloatBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Queue;
import java.util.Vector;

import main.Renderer;

import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL13;
import org.lwjgl.opengl.GL20;

public class RenderGroup {
	Queue<Model> models;
	public int program;
	public boolean inWorld;
	int texture;
	public boolean useCam;
	public RenderGroup(int tex) {
		models = new LinkedList<Model>();
		texture = tex;
		program = Renderer.COORD3_TEX2;
		inWorld = true;
		useCam = true;
	}
	public RenderGroup() {
		models = new LinkedList<Model>();
		texture = -1;
		program = Renderer.COORD3_CLR3;
		inWorld = true;
		useCam = true;
	}
	public void add(Model mod) {
		if (mod != null) {
			if (program == Renderer.COORD3_CLR3 && mod.getPosVect().size() > mod.getTexVect().size())
				mod.color(1.0, 0.0, 0.0);
			models.add(mod);
		}
	}
	FloatBuffer getModelsVerts() {
		int size = 0;
		int i;
		int coord;
		Iterator<Model> modIter = models.iterator();
		Model curMod;
		for (i=0;i < models.size();i++)
			size+=modIter.next().getPosVect().size();
		FloatBuffer buf = BufferUtils.createFloatBuffer(size);
		modIter = models.iterator();
		for (i=0;i < models.size();i++) {
			curMod = modIter.next();
			for (coord=0;coord < curMod.getPosVect().size();coord++)
				buf.put(curMod.getPosVect().get(coord));
		}
		return buf;
	}
	FloatBuffer getModelsTexCoords() {
		int size = 0;
		int i;
		int coord;
		Iterator<Model> modIter = models.iterator();
		Model curMod;
		for (i=0;i < models.size();i++)
			size+=modIter.next().getTexVect().size();
		FloatBuffer buf = BufferUtils.createFloatBuffer(size);
		modIter = models.iterator();
		for (i=0;i < models.size();i++) {
			curMod = modIter.next();
			for (coord=0;coord < curMod.getTexVect().size();coord++)
				buf.put(curMod.getTexVect().get(coord));
		}
		return buf;
	}
	public void render() {
		FloatBuffer tex_buf = null;
	    FloatBuffer vert_buf = getModelsVerts();
	    vert_buf.flip();
    	tex_buf = getModelsTexCoords();
    	tex_buf.flip();
		GL11.glEnable(GL11.GL_DEPTH_TEST);
		GL11.glDepthFunc(GL11.GL_LEQUAL);
		if (!inWorld || !useCam)
			GL20.glUniform3f(Renderer.cam_rot_pos, 0.0f,0.0f,0.0f);
		else
			GL20.glUniform3f(Renderer.cam_rot_pos, (float)Renderer.camRotX, (float)Renderer.camRotY, (float)Renderer.camRotZ);
		if (!inWorld || !useCam)
			GL20.glUniform3f(Renderer.cam_pos_pos, 0.0f,0.0f,0.0f);
		else
			GL20.glUniform3f(Renderer.cam_pos_pos, (float)Renderer.camPosX, (float)Renderer.camPosY, (float)Renderer.camPosZ);
		if (!inWorld)
			GL20.glUniform1i(Renderer.fade_pos, 0);
		else
			GL20.glUniform1i(Renderer.fade_pos, 1);
		if (texture != -1) {
			GL13.glActiveTexture(GL13.GL_TEXTURE1);
			GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
			GL11.glTexParameterf(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		}
		GL20.glVertexAttribPointer(Renderer.coord_pos, 3, false, 0, vert_buf);
		GL20.glEnableVertexAttribArray(Renderer.coord_pos);
		
		if (program == Renderer.COORD3_TEX2) {
			GL20.glVertexAttribPointer(Renderer.tex_coord_pos, 2, false, 0, tex_buf);
			GL20.glEnableVertexAttribArray(Renderer.tex_coord_pos);
			GL20.glUniform1i(Renderer.tex_pos, 1);
		} else {
			GL20.glVertexAttribPointer(Renderer.color_pos, 3, false, 0, tex_buf);
			GL20.glEnableVertexAttribArray(Renderer.color_pos);
		}
		GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, vert_buf.limit()/3);  //limit beacause we flipped
		GL20.glDisableVertexAttribArray(Renderer.coord_pos);
		GL20.glDisableVertexAttribArray(Renderer.tex_coord_pos);
		GL11.glDisable(GL11.GL_DEPTH_TEST);
	}
}
