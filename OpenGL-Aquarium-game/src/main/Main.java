package main;
import java.awt.FontFormatException;
import java.io.IOException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import model.Cylinder;
import model.DeathScreen;
import model.Model;
import model.Rectangle;
import model.RenderGroup;
import model.SeaFloor;
import model.TextModel;
import model.WinScreen;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import sound.SoundMgr;


public class Main {
	public static void main(String[] args) throws LWJGLException, InterruptedException, IOException, FontFormatException, UnsupportedAudioFileException, LineUnavailableException {
		double k=1.0;
		double x,y;
		Model barrel = new Cylinder(0.1,0.3);
		Renderer.init();
		SoundMgr.init();
		TextModel hi = Renderer.font.getStringModel("Hi!");
		Model rect = new Rectangle(0.2,0.2);
		Model floor = new SeaFloor();
		floor.scale(4.0);
		floor.move(-2.0, -1.0, 0.0);
		hi.setPos(-0.2,0.0,1.0);
		barrel.setPos(-0.0, -0.2, 0.5);
		while (!Display.isCloseRequested()) {
			Thread.sleep(100);
			Renderer.clear();
			if (Keyboard.isKeyDown(Keyboard.KEY_A))
				Renderer.camRotY-=0.1;
			if (Keyboard.isKeyDown(Keyboard.KEY_D))
				Renderer.camRotY+=0.1;
			if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
				Renderer.camPosX+=0.1*Model.frontVector(Renderer.camRotX, Renderer.camRotY, Renderer.camRotZ)[0];
				Renderer.camPosZ+=0.1*Model.frontVector(Renderer.camRotX, Renderer.camRotY, Renderer.camRotZ)[2];
			}
			if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
				Renderer.camPosX-=0.1*Model.frontVector(Renderer.camRotX, Renderer.camRotY, Renderer.camRotZ)[0];
				Renderer.camPosZ-=0.1*Model.frontVector(Renderer.camRotX, Renderer.camRotY, Renderer.camRotZ)[2];
			}
			x = -1.0f+2.0*(double)Mouse.getX()/Display.getWidth();
			y = -1.0f+2.0*(double)Mouse.getY()/Display.getHeight();
			k+=0.1;
			
			RenderGroup rg = new RenderGroup();
			RenderGroup text = new RenderGroup(Renderer.font.texture);
			RenderGroup radio = new RenderGroup(TexUtils.radioactive);
			hi.setPos(x, y, 1.0);
			hi.rotate(0.0,0.05,0.05);
			text.add(hi);
			text.inWorld = false;

			//barrel.scale(1.1);
			//if (y > 0.9)
			//	SoundMgr.bite.play();
			rect.setPos(0.0,0.0,1.0);
			//rect.color(Math.sin(k*3.1)/2.0+0.5,Math.cos(k*3.1)/2.0-0.5, 0.0);
			rect.color(0.0, 1.0, 1.0);
			barrel.rotate(0.0, 0.1, 0.1);
			radio.add(barrel);
			rg.add(rect);
			rg.add(floor);
			rg.inWorld = true;
			Renderer.addGroup(radio);
			Renderer.addGroup(rg);
			Renderer.addGroup(text);
			//if (Renderer.getGameTimeMillis() > 10000)
			//	WinScreen.show();
			//Renderer.camRotY+=0.05;
			//Renderer.camPosZ+=y/10;
			Renderer.render();
			Display.setTitle("render FPS:"+1000000000/Renderer.getFrameTime());
		}
	}
}
