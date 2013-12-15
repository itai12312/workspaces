package game;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.lwjgl.opengl.Display;

import main.Renderer;
import model.RenderGroup;

public class Enemy {
	final int num = 3;
	Enem[] enem = new Enem[num];
	RenderGroup enemies = new RenderGroup();

	public void start(int locationx,int locationy) throws IOException, UnsupportedAudioFileException,
			LineUnavailableException {
		for (int i = 0; i < num; i++) {
			enem[i] = new Enem();
			int enemyX = Hash.hash3("xx00" +locationx+ i) % Display.getWidth();
			int enemyY = Hash.hash3("yy0 0 "+locationy+ i) % Display.getHeight();
			enem[i].coords2[0] = xtoX(enemyX, 0.4);
			enem[i].coords2[1] = ytoY(enemyY, 0.4);
			enem[i].coords2[2] = 1.0;
			enem[i].createmodel();
			enemies.add(enem[i].mode);
		}
		// gen(0,0);
		damage(0,0);
	}

	public double distance(double x, double y, double z, int i) {
		return enem[i].distance(x, y, z);
	}

	public void damage(int locationx,int locationy) throws IOException, UnsupportedAudioFileException,
			LineUnavailableException {
		enemies = new RenderGroup();
		for (int i = 0; i < num; i++) {
			enem[i].damage(Player.x, Player.y, Player.z);
			if (distance(Player.x, Player.y, Player.z, i) < enem[i].radius) {
				enem[i].seekPlayer(i,locationx,locationy);
				if (!enem[i].isdead)
					enemies.add(enem[i].mode);
			} else {
					enem[i].update1(locationx,locationy,i);
				enem[i].mode.setPos(enem[i].coords2[0], enem[i].coords2[1],enem[i].coords2[2]);
				if (!enem[i].isdead)
					enemies.add(enem[i].mode);
			}
		}
	}

	public void gen(int shiftx1, int shifty1) {
		System.out.println("gen" + shiftx1 + " " + shifty1);
		enemies = new RenderGroup();
		for (int i = 0; i < num; i++) {
			int enemyX = Hash.hash3("" + shiftx1 + " " + shifty1 + " " + i)
					% Display.getWidth();
			int enemyY = Hash.hash3("yy" + shiftx1 + " " + shifty1 + " " + i)
					% Display.getHeight();
			if(enem[i]!=null){
			enem[i].coords2[0] = xtoX(enemyX, 0.4);
			enem[i].coords2[1] = ytoY(enemyY, 0.4);
			enem[i].coords2[2] = 1.0;
			enem[i].createmodel();
			if (enem[i].isdead){
				enem[i].isdead=false;
				enem[i].health=100;
			}
			enemies.add(enem[i].mode);
			}
		}
	}

	public double xtoX(int x, double z) {
		return ((z * (x - Display.getWidth() / 2)) / (Display.getWidth() / 2));
	}

	public double ytoY(int x, double z) {
		return ((z * (x - Display.getHeight() / 2)) / (Display.getHeight() / 2));
	}
}
