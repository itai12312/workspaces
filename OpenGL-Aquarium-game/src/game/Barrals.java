package game;
import java.io.IOException;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import org.lwjgl.opengl.Display;

import main.TexUtils;
import model.RenderGroup;
public class Barrals {
	final int num=3;
	Barral[]enem=new Barral[num];
	RenderGroup enemies=new RenderGroup(TexUtils.radioactive);
	public void start() throws IOException, UnsupportedAudioFileException, LineUnavailableException{
		for( int i =0;i < num;i++ ) {
			enem[i]=new Barral();
			int enemyX = Hash.hash3("zxx00"+i)%Display.getWidth();
			//int enemyY = Hash.hash3("zyy0 0 "+i)%Display.getHeight();
			enem[i].coords2[0]=xtoX(enemyX,0.4);
			enem[i].coords2[1]=-0.6;
			enem[i].coords2[2]=1.0;
			enem[i].createmodel();
			enemies.add(enem[i].mode);
		}
	}
	private  void damage(double x,double y,double z,int i) throws IOException, UnsupportedAudioFileException, LineUnavailableException{
		enem[i].damage(x,y,z);
	}
	public  void gen(int shiftx1,int shifty1){
		enemies = new RenderGroup(TexUtils.radioactive);
		//System.out.println("gen"+shiftx1+" "+shifty1);
		for( int i =0;i <num;i++ ) {
			int enemyX = Hash.hash3("z"+shiftx1+" "+shifty1+" "+i)%Display.getWidth();
			int enemyY = Hash.hash3("zzyy"+shiftx1+" "+shifty1+" "+i)%Display.getHeight();
			enem[i].coords2[0]=xtoX(enemyX,0.4);
			enem[i].coords2[1]=-0.6;
			if(shifty1!=0)
				enem[i].coords2[1]=ytoY(enemyY,0.4);
			enem[i].coords2[2]=1.0;
			enem[i].createmodel();
			enemies.add(enem[i].mode);
		}
	}
	public double xtoX(int x,double z){
		return ((z*(x-Display.getWidth()/2))/(Display.getWidth()/2));
	}
	public double ytoY(int x,double z){
		return ((z*(x-Display.getHeight()/2))/(Display.getHeight()/2));
	}
	public void damage() {
		for(int i=0;i<num;i++){
			try {
				damage(Player.x,Player.y,Player.z,i);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (UnsupportedAudioFileException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (LineUnavailableException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}

	}
}