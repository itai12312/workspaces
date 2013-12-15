package game;
import objread.GLModel;
import objread.GL_Triangle;
import model.Model;
import model.RenderGroup;
import model.TriangleMesh;
public class Player extends Model{
	static RenderGroup play=new RenderGroup();
	private static Model l;
	static int fisheaten=0;
	static boolean isdead=false;
	static String path="models/3.obj";
	static double x=0.0,y=0.0,z=1.0,size=1,rotatex=0.0,rotatey=0.0,speed=0.7,health=100;
	static double rz=0.0;
	public static void start(){
		GLModel g=new GLModel(path);
		GL_Triangle[]p=g.loadMesh(path).triangles;
		l = new TriangleMesh(p);
		
		l.setScale(0.003);
		//l.move(0.0,0.0,-0.06);
		l.fixTrasform();
		l.color(0.5, 0.3, 0.8);
		l.rotate(rotatex, rotatey, rz);
		play.add(l);
	}
	public static void redraw(boolean dir){
		if(Player.health<1){
			isdead=true;
		}else{
			isdead=false;
		}
		play=new RenderGroup();
		if(dir)
			l.setAngle(0.0, 0.0, 0.0);
		else
			l.setAngle(0.0, Math.PI, 0.0);
		l.setScale(size);
		l.setPos(x, y, z);
		l.rotate(rotatex, rotatey, rz);
		play.add(l);
	}
	public static void restart(){
		health=100;
		isdead=false;
		speed=0.5;
		rotatex=0.0;rotatey=0.0;rz=0.0;		
		health=100;//on click display health, o=death, 100=full
		x=0.0;y=0.0;z=1.0;
		size=1;
		start();
	}
}