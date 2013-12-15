package game;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import model.Cylinder;
import model.Model;
public class Barral {
	Model mode;
	int factor1=50;
	double fac1=0.25;
	int health=100;
	double[]coords2=new double[3];
	double radius=0.1;
	double factor=10;
	String path="models\\2.obj";
	public void createmodel(){
		mode=new Cylinder(0.08, 0.15);
		mode.setPos(coords2[0], coords2[1], coords2[2]);
	}
	public double distance(double x, double y, double z) {
		return (x-coords2[0])*(x-coords2[0])+(y-coords2[1])*(y-coords2[1])+(z-coords2[2])*(z-coords2[2]);
	}
	public void damage(double x, double y, double z) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
		if(distance(x,y,z)<radius){
			Player.speed+=Math.random()*0.09*fac1;
			Player.size+=0.1;
			if(Player.size>1.2)
				Player.size=1.2;
		}
		if(distance(x,y,z)<radius/2)
			Player.speed-=Math.random()*0.09*fac1;
		if(Player.speed>5)
			Player.speed=5;
	}
}