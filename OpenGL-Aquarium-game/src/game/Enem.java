package game;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import org.lwjgl.opengl.Display;

import objread.GLModel;
import objread.GL_OBJ_Importer;
import objread.GL_Triangle;
import sound.SoundMgr;
import main.Renderer;
import model.Model;
import model.TriangleMesh;
public class Enem {
	Model mode;
	boolean isdead=false;
	double[]coords2=new double[3];
	double fact=0.1,hardness=1,factor=10,radius=0.05,health=100,factor1=50,damage,radius1=0.25;
	String path="models/2.obj";
	double alpha=0;
	double cycletime=5.0;//seconds
	public  void createmodel(){
		//GLModel g=new GLModel(Thread.currentThread().getContextClassLoader().getResourceAsStream(path));
		GL_Triangle[]p=new GL_OBJ_Importer().importFromStream(Thread.currentThread().getContextClassLoader().getResourceAsStream(path)).triangles;
		mode = new TriangleMesh(p);
		mode.scale(0.001);
		fact=0.1;hardness=1;factor=10;radius=0.05;health=100;factor1=50;alpha=0;
		damage=Math.random();
		mode.color(0.0, damage, 0.0);
		mode.scale(Math.max(0.5, health)/100);
		mode.setPos(coords2[0], coords2[1], coords2[2]);
		damage=damage*7;
	}
	public double distance(double x, double y, double z) {
		return (x-coords2[0])*(x-coords2[0])+(y-coords2[1])*(y-coords2[1])+(z-coords2[2])*(z-coords2[2]);
	}
	
	public void damage(double x, double y, double z) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		hardness+=(float)(Renderer.getFrameTime()/1000000000)/300;
		if(distance(x,y,z)<radius&&!isdead){
			Player.health-=Math.min(Math.random()*fact,1.5)*damage*hardness;
			health-=Math.min(Math.random()*fact,2)*3*damage;
			Player.size-=0.1;
			SoundMgr.bite.play();
			if(Player.size<0.9)
				Player.size=0.9;
			//if(Player.health<0)
				//SoundMgr.bite.play();
		}if(health<1&&!isdead){
			Player.fisheaten++;
			isdead=true;
			Player.health+=10;
		}
	}
	public void seekPlayer(int i,int shiftx1,int shifty1){
		boolean left=true;
		if(!isdead){
		if((Player.x-coords2[0])>0)
			left=false;
		coords2[0]=coords2[0]+(Player.x-coords2[0])/factor1;
		coords2[1]=coords2[1]+(Player.y-coords2[1])/factor1;
		coords2[2]=coords2[2]+(Player.z-coords2[2])/factor1;
		mode.setPos(coords2[0],coords2[1],coords2[2]);
		if(!left)
			mode.setAngle(0, Math.PI, 0);
		else
			mode.setAngle(0, 0, 0);
		}
	}
	public double xtoX(int x, double z) {
		return ((z * (x - Display.getWidth() / 2)) / (Display.getWidth() / 2));
	}

	public double ytoY(int x, double z) {
		return ((z * (x - Display.getHeight() / 2)) / (Display.getHeight() / 2));
	}
	public void update1(int locationx,int locationy,int i) {
		if(i==0){
		int enemyX = Hash.hash3("" + locationx + " " + locationy + " " + 0)
				% Display.getWidth();
		int enemyY = Hash.hash3("yy" + locationx + " " + locationy + " " + 0)
				% Display.getHeight();
			alpha+=(Renderer.getFrameTime()/1000000000.0)*360/cycletime;
		//	System.out.println((Renderer.getFrameTime()/1000000000.0));
			if (alpha>360)
				alpha-=360;
			coords2[0]=xtoX(enemyX, 0.4)+radius1*Math.sin(alpha*Math.PI/180);
			coords2[1]=ytoY(enemyY, 0.4)+radius1*Math.cos(alpha*Math.PI/180);
		}else{
		if((int)(Math.random()*80)==15){
			coords2[0]=coords2[0]+0.25*(0.5+2*(Math.random()-0.5));
			coords2[1]=coords2[1]+0.25*(0.5+2*(Math.random()-0.5));
			//coords2[2]=coords2[2]+Math.random()*0.5;
			if(coords2[0]>0.98)
				coords2[0]=0.98;
			else if(coords2[0]<-0.98)
				coords2[0]=-0.98;
			if(coords2[1]>0.98)
				coords2[1]=0.98;
			else if(coords2[1]<-0.98)
				coords2[1]=-0.98;
		}
		}
	}
}