package game;
import java.awt.FontFormatException;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import main.Renderer;
import model.DeathScreen;
import model.Model;
import model.RenderGroup;
import model.SeaFloor;
import model.WinScreen;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.XRandR.Screen;

import sound.SoundMgr;
public class main2/* extends app*/{
	double scale=800/35;
	double increaseby=(Math.sqrt(Display.getHeight()*Display.getHeight()+Display.getWidth()*Display.getWidth()))/scale*0.01*0.5;
	
	int locationx=600,locationy=0;

	boolean left=true,change=false,restart=false,win=false;
	boolean camerascreen=true,camerachange=false;

	Enemy e1=new Enemy();
	Barrals barrals=new Barrals();
	Model seaFloor = new SeaFloor();
	public static void main(String[] args) throws UnsupportedAudioFileException, LineUnavailableException, IOException{
		try {
			Renderer.init();
			SoundMgr.init();
			GL11.glDisable(GL11.GL_DEPTH_TEST);
		} catch (LWJGLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (FontFormatException e) {
			e.printStackTrace();
		}
		Player.start();
		main2 m=new main2();
	}
	public main2() throws IOException, UnsupportedAudioFileException, LineUnavailableException{
		reset();
		run();
	}
	public void reset() throws IOException, UnsupportedAudioFileException, LineUnavailableException{
		e1.start(locationx,locationy);
		barrals.start();
		locationx=600*(int)(Math.random()*50);
		locationy=0;
		moveRegion();
	}
	public void run(){
		try {
			while (!Display.isCloseRequested()) {
				handleEvents();     // call key...() and mouse...() functions based on input events
				update();           // do program logic here (subclass may override this)
				render();             // redraw the screen (subclass overrides this)
				Thread.sleep(8);
			}
		}
		catch (Exception e) {
			e.printStackTrace(System.out);
		}
	}
	public void handleEvents() throws IOException, UnsupportedAudioFileException, LineUnavailableException{
		double fac=0.03*Player.speed;
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)){ Player.x-=0.1*fac;left=true;}
		else if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){ Player.x+=0.10*fac;left=false;}
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)){ 
			Player.y+=0.1*fac;}
		else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)){ Player.y-=0.1*fac;}
		if(Keyboard.isKeyDown(Keyboard.KEY_O)){ Player.z+=0.7*fac;
		Renderer.camPosX+=0.1*Model.frontVector(Renderer.camRotX, Renderer.camRotY, Renderer.camRotZ)[0];
		Renderer.camPosY+=0.1*Model.frontVector(Renderer.camRotX, Renderer.camRotY, Renderer.camRotZ)[1];
		Renderer.camPosZ+=0.1*Model.frontVector(Renderer.camRotX, Renderer.camRotY, Renderer.camRotZ)[2];}
		else if(Keyboard.isKeyDown(Keyboard.KEY_L)){ Player.z-=0.7*fac;
		Renderer.camPosX-=0.1*Model.frontVector(Renderer.camRotX, Renderer.camRotY, Renderer.camRotZ)[0];
		Renderer.camPosY-=0.1*Model.frontVector(Renderer.camRotX, Renderer.camRotY, Renderer.camRotZ)[1];
		Renderer.camPosZ-=0.1*Model.frontVector(Renderer.camRotX, Renderer.camRotY, Renderer.camRotZ)[2];}

		if(Keyboard.isKeyDown(Keyboard.KEY_Z)){
			Player.rotatex+=increaseby*(0.15/0.35)*0.07;
		}if(Keyboard.isKeyDown(Keyboard.KEY_1)){
			Player.rotatex-=increaseby*(0.15/0.35)*0.07;
		}if(Keyboard.isKeyDown(Keyboard.KEY_X)){
			Player.rotatey+=increaseby*(0.15/0.35)*0.07;
		}if(Keyboard.isKeyDown(Keyboard.KEY_2)){
			Player.rotatey-=increaseby*(0.15/0.35)*0.07;
		}if(Keyboard.isKeyDown(Keyboard.KEY_C)){
			Player.rz+=increaseby*(0.15/0.35)*0.07;
		}if(Keyboard.isKeyDown(Keyboard.KEY_3)){
			Player.rz-=increaseby*(0.15/0.35)*0.07;
		}
		double fact3=0.05;
		if(Keyboard.isKeyDown(Keyboard.KEY_RETURN)){
			if(Player.isdead||win)
				restart=true;
		}if(Keyboard.isKeyDown(Keyboard.KEY_Q)){
			Renderer.camPosZ-=0.1*fact3;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_E)){
			Renderer.camPosZ+=0.1*fact3;
		}if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			Renderer.camPosY+=0.1*fact3;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			Renderer.camPosY-=0.1*fact3;
		}if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			Renderer.camPosX-=0.1*fact3;
		}else if(Keyboard.isKeyDown(Keyboard.KEY_D)){	
			Renderer.camPosX+=0.1*fact3;
		}if(Keyboard.isKeyDown(Keyboard.KEY_F)){
			Renderer.camRotX+=0.1*fact3;
		}if(Keyboard.isKeyDown(Keyboard.KEY_V)){
			Renderer.camRotX-=0.1*fact3;
		}if(Keyboard.isKeyDown(Keyboard.KEY_G)){
			Renderer.camRotY+=0.1*fact3;
		}if(Keyboard.isKeyDown(Keyboard.KEY_B)){
			Renderer.camRotY+=0.1*fact3;
		}if(Keyboard.isKeyDown(Keyboard.KEY_H)){	
			Renderer.camRotZ-=0.1*fact3;
		}if(Keyboard.isKeyDown(Keyboard.KEY_N)){	
			Renderer.camRotZ-=0.1*fact3;
		}if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)){
			System.exit(0);
		}
		
		
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()){
				if(Keyboard.getEventKey() == Keyboard.KEY_SPACE){
					camerascreen=!camerascreen;
					camerachange=true;
					cameraChange(camerascreen);
				}
			}
		}
		if(camerascreen){
			System.out.println(Player.x+" "+(Player.z-Renderer.camPosZ));
		if (Player.x < -1*(Player.z-Renderer.camPosZ)) {
			Player.x = 1*(Player.z-Renderer.camPosZ);
			locationx-=Display.getWidth();
			moveRegion();
		}else if (Player.x > 1*(Player.z-Renderer.camPosZ)) {
			Player.x = -1.0*(Player.z-Renderer.camPosZ);
			locationx+=Display.getWidth();
			moveRegion();
		}if (Player.y < -1*(Player.z-Renderer.camPosZ)) {
			Player.y = 1.0*(Player.z-Renderer.camPosZ);
			locationy-=Display.getHeight();//=0;
			moveRegion();
		}else if (Player.y > 1*(Player.z-Renderer.camPosZ)) {
			Player.y = -1.0*(Player.z-Renderer.camPosZ);
			locationy+=Display.getHeight();
			moveRegion();
		}
		}
		if (locationy == 0 && Player.y < -0.8)
			Player.y=-0.8;
		if(Keyboard.isKeyDown(Keyboard.KEY_Y)){
			Player.x=Player.z*(-1.0f+2.0f*(float)Mouse.getX()/Display.getWidth())*(Player.z-Renderer.camPosZ);//x
			Player.y=Player.z*(-1.0f+2.0f*(float)Mouse.getY()/Display.getHeight())*(Player.z-Renderer.camPosZ);//y
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_M)){
			Renderer.camPosX=0;
			Renderer.camPosY=0;
			Renderer.camPosZ=0;
			Renderer.camRotX=0;
			Renderer.camRotY=0;
			Renderer.camRotZ=0;
		}	
		
		if(Keyboard.isKeyDown(Keyboard.KEY_4)){
			Player.speed-=0.1;
		}if(Keyboard.isKeyDown(Keyboard.KEY_5)){
			Player.speed+=0.1;
		}
		cameraChange(camerascreen);
	}
	public void moveRegion() {
		e1.gen(locationx, locationy);
		barrals.gen(locationx, locationy);
		seaFloor = new SeaFloor();
		seaFloor.scale(1.3);
		seaFloor.move(-0.8, -0.8-locationy, 0.0);
	}
	public void update() throws IOException, UnsupportedAudioFileException, LineUnavailableException{
		if(Player.fisheaten>6)
			win=true;
		if (camerascreen)
			Player.redraw(left);
		else
			Player.redraw(true);
		e1.damage(locationx,locationy);
		barrals.damage();
		String s=""+Player.speed;
		s=s.substring(0,3);
		String y=""+Player.health;
		y=y.substring(0, 4);
		if(Renderer.getFrameTime()!=0)
			Display.setTitle("Health: "+y+" Speed: "+s+" FPS: "+1000000000/Renderer.getFrameTime());
	}
	private void cameraChange(boolean camerascreen2) {
		if(!camerascreen2){
			
			Renderer.camRotX=Player.rotatex;
			Renderer.camRotY=Player.rotatey+Math.PI/2;
			Renderer.camRotZ=-Player.rz;
			Renderer.camPosX=Player.x+0.3;
			Renderer.camPosY=Player.y;
			Renderer.camPosZ=Player.z;
			System.out.println("x"+Model.frontVector(Renderer.camRotX, Renderer.camRotY, Renderer.camRotZ)[0]);
			System.out.println("y"+Model.frontVector(Renderer.camRotX, Renderer.camRotY, Renderer.camRotZ)[1]);
			System.out.println("z"+Model.frontVector(Renderer.camRotX, Renderer.camRotY, Renderer.camRotZ)[2]);
			//Renderer.camPosX+=0.4*Model.frontVector(Renderer.camRotX, Renderer.camRotY, Renderer.camRotZ)[0];
			//Renderer.camPosY+=0.4*Model.frontVector(Renderer.camRotX, Renderer.camRotY, Renderer.camRotZ)[1];
			//Renderer.camPosZ+=0.4*Model.frontVector(Renderer.camRotX, Renderer.camRotY, Renderer.camRotZ)[2];
		}else{
			//Renderer.camPosX=0;
			//Renderer.camPosY=0;
			//Renderer.camPosZ=0.2;
		}
	}
	public void render() throws IOException, UnsupportedAudioFileException, LineUnavailableException{
		//rotation += secondsSinceLastFrame * 90f;
		Renderer.clear();
		if(restart){
			e1.start(locationx,locationy);
			Player.restart();
			reset();
			restart=false;
			win=false;
			Player.fisheaten=0;
			Renderer.reset();
		}if(win){
			WinScreen.show();
		}else if(!Player.isdead){
			RenderGroup floor = new RenderGroup();
			floor.add(seaFloor);
			Renderer.addGroup(floor);
			Renderer.addGroup(barrals.enemies);
			Renderer.addGroup(e1.enemies);
			Renderer.addGroup(Player.play);
		}else{
			DeathScreen.show();
		}
		Renderer.render();
	}	
}