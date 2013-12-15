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
import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import sound.SoundMgr;
public class main1/* extends app*/{
	double rotate=0;
	double scale=800/35;
	double increaseby=(Math.sqrt(Display.getHeight()*Display.getHeight()+Display.getWidth()*Display.getWidth()))/scale*0.01*0.5;
	double[][][]coord;
	
	int locationx=600,locationy=0;
	int[]textures;
	
	boolean rotation=true;
	boolean left=true,change=false;
	boolean restart=false;
	
	Enemy e1=new Enemy();
	Barrals barrals=new Barrals();
	String[][] paths;
	RenderGroup[]rendergroup;
	Model[][]models;
	String[][] labels;
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
		int k=1;
		String[][]path={{"",""}};
		double[][][]coords=new double[k][2][3];
		coords[0][0][0]=0.0;coords[0][0][1]=0.0;coords[0][0][2]=0.5;
		String[][]labels={{"Hi!",""}};
		int[]textures=new int[k];
		textures[0]=Renderer.font.texture;
		main1 m=new main1(k,2,path,coords,labels,textures);
	}
	public main1(int sizeofrendergroup,int sizeofmodels,String[][]paths,double[][][]coord,String[][] labels,int[]textures) throws IOException, UnsupportedAudioFileException, LineUnavailableException{
	//	e1.start();
		barrals.start();
		locationx=600*(int)(Math.random()*50);
		locationy=0;
		this.labels=labels;
		this.coord=coord;
		this.textures=textures;
		rendergroup=new RenderGroup[sizeofrendergroup];
		models=new Model[sizeofrendergroup][sizeofmodels];
		this.paths=paths;
		moveRegion();
		/*for(int i=0;i<rendergroup.length;i++){
			if(textures[i]>-1)
				rendergroup[i]=new RenderGroup(textures[i]);//texture num
			else
				rendergroup[i]=new RenderGroup();
		}
		for(int i=0;i<labels.length;i++){
			for(int j=0;j<labels[i].length;j++){
				if(labels[i][j]!=null&&labels[i][j]!=""){
					models[i][j]=Renderer.font.getStringModel(labels[i][j]);
					rendergroup[i].add(models[i][j]);
				}else if(paths[i][j]!=null&&paths[i][j]!=""){
						GLModel g=new GLModel(paths[i][j]);
						GL_Triangle[]p=g.loadMesh(paths[i][j]).triangles;
						models[i][j] = new TriangleMesh(p);
						rendergroup[i].add(models[i][j]);
				}
			}
		}*/
		run();
	}
	public void run(){
			try {
			while (!Display.isCloseRequested()) {
				handleEvents();     // call key...() and mouse...() functions based on input events
				update();           // do program logic here (subclass may override this)
				render();             // redraw the screen (subclass overrides this)
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
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)){ Player.y+=0.1*fac;}
		else if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)){ Player.y-=0.1*fac;}
		if(Keyboard.isKeyDown(Keyboard.KEY_O)) Player.z+=0.7*fac;
		else if(Keyboard.isKeyDown(Keyboard.KEY_L)) Player.z-=0.7*fac;
		
		if(Keyboard.isKeyDown(Keyboard.KEY_P)){
			rotate+=increaseby*(0.15/0.35)*0.07;
			rotation=true;
		//	Player.rotate=rotate;
		}

		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()){
				if(Keyboard.getEventKey()==Keyboard.KEY_RETURN){
					rotate=0;
					rotation=false;
					if(Player.isdead)
						restart=true;
				}
				else if(Keyboard.getEventKey()==Keyboard.KEY_Q){
					Renderer.camPosZ+=0.1;
				}else if(Keyboard.getEventKey()==Keyboard.KEY_E){
					Renderer.camPosZ-=0.1;
				}else if(Keyboard.getEventKey()==Keyboard.KEY_W){
					Renderer.camPosY-=0.1;
				}else if(Keyboard.getEventKey()==Keyboard.KEY_S){
					Renderer.camPosY+=0.1;
				}else if(Keyboard.getEventKey()==Keyboard.KEY_A){
					Renderer.camPosX+=0.1;
				}else if(Keyboard.getEventKey()==Keyboard.KEY_D){	
					Renderer.camPosX-=0.1;
				}else if(Keyboard.getEventKey()==Keyboard.KEY_ESCAPE){
					System.exit(0);
				}else{
					//rotate=0;//rotation=false;
				}
			}
		}

		if (Player.x < -1) {
			Player.x = 1.0;
			locationx-=Display.getWidth();
			moveRegion();
		}else if (Player.x > 1) {
			Player.x = -1.0;
			locationx+=Display.getWidth();
			moveRegion();
		}if (Player.y < -1) {
			Player.y = 1.0;
			locationy-=Display.getHeight();//=0;
			moveRegion();
		}else if (Player.y > 1) {
			Player.y = -1.0;
			locationy+=Display.getHeight();
			moveRegion();
		}
		
		if (locationy == 0 && Player.y < -0.8)
			Player.y=-0.8;
		
		if(Keyboard.getEventKey()==Keyboard.KEY_Y){
			Player.x=Player.z*(-1.0f+2.0f*(float)Mouse.getX()/Display.getWidth());//x
			Player.y=Player.z*(-1.0f+2.0f*(float)Mouse.getY()/Display.getHeight());//y
		}
		Player.redraw(left);
		///e1.damage();
		barrals.damage();
	}
	public void moveRegion() {
		e1.gen(locationx, locationy);
		barrals.gen(locationx, locationy);
		seaFloor = new SeaFloor();
		seaFloor.scale(1.6);
		seaFloor.move(-0.8, -0.8-locationy, 0.0);
	}
	public void update(){}
	public void render(){
		//rotation += secondsSinceLastFrame * 90f;
		Renderer.clear();
		if(!Player.isdead){
		RenderGroup r=new RenderGroup(Renderer.font.texture);
		Model health=Renderer.font.getStringModel("Health"+Player.health);
		health.setPos(0.6, 0.7, 1.0);
		Model sp=Renderer.font.getStringModel("Speed"+Player.speed);
		sp.setPos(-0.6, 0.8, 1.0);
		r.add(sp);
		r.add(health);
		r.inWorld = false;
		Renderer.addGroup(r);
		RenderGroup floor = new RenderGroup();
		floor.add(seaFloor);
		Renderer.addGroup(floor);
		Renderer.addGroup(barrals.enemies);
		Renderer.addGroup(e1.enemies);
		Renderer.addGroup(Player.play);
		}else{
			DeathScreen.show();
		}
		/*for(int i=0;i<models.length;i++){
			for(int j=0;j<models[i].length;j++){
				if(models[i][j]!=null){
					models[i][j].setPos(coord[i][j][0],coord[i][j][1],coord[i][j][2]);
				}
			}
		}
		for(int i=0;i<models.length-1;i++){
			if(textures[i]>-1)
				rendergroup[i]=new RenderGroup(textures[i]);//texture num
			else
				rendergroup[i]=new RenderGroup();
			for(int j=0;j<models[i].length;j++){
				if(models[i][j]!=null)
					rendergroup[i].add(models[i][j]);
			}
			Renderer.addGroup(rendergroup[i]);
		}*/
		
		Renderer.render();
	}

	public double xtoX(int x,double z){
		return ((z*(x-Display.getWidth()/2))/(Display.getWidth()/2));
	}
	public double ytoY(int x,double z){
		return ((z*(x-Display.getHeight()/2))/(Display.getHeight()/2));
	}
	public int Xtox(double x,double z){
		if(x==Display.getWidth()/2)
			return 0;
		return (int)(Display.getWidth()/2*x/z)+400;
	}
	public int Ytoy(double x,double z){
		if(x==Display.getHeight()/2)
			return 0;
		return (int)(Display.getHeight()/2*x/z)+400;
	}	
}