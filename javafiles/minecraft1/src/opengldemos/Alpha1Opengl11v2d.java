package opengldemos;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
public class Alpha1Opengl11v2d{
	static double colorred=0.5;
	static double colorgreen=0.5;
	static double colorblue=0.5;
	/** init position of quad */
	int height=600;
	int width=800;
	float x = 400, y = 300;
	double xtorotate,ytorotate;
	float absolutex=400;
	float absolutey=300;
	float rotate=0;
	boolean rotation=true;
	float locationx=0;
	float locationy=0;
	/** angle of quad rotation */
	double zoomin=1;
	double scale=800/35;
	double increaseby=(Math.sqrt(height*height+width*width))/scale*0.01*0.5;
	/** time at last frame */
	long lastFrame;
 
	/** frames per second */
	int fps;
	/** last fps time */
	long lastFPS;
	
	/** is VSync Enabled */
	boolean vsync;
	 
	public void start() {
		try {
			Display.setDisplayMode(new DisplayMode(width, height));
			Display.create();
		} catch (LWJGLException e) {
			e.printStackTrace();
			System.exit(0);
		}
 
		initGL(); // init OpenGL
		getDelta(); // call once before loop to initialise lastFrame
		lastFPS = getTime(); // call before loop to initialise fps timer
 
		while (!Display.isCloseRequested()) {
			int delta = getDelta();
 
			update(delta);
			renderGL();
 
			Display.update();
			Display.sync(60); // cap fps to 60fps
		}
 
		Display.destroy();
	}
	
	public void update(int delta) {
		// rotate quad
		//rotate += 0.15f * delta;
		
		if (Keyboard.isKeyDown(Keyboard.KEY_LEFT)){ absolutex -= increaseby * delta;}
		if (Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){ absolutex += increaseby * delta;}
 
		if (Keyboard.isKeyDown(Keyboard.KEY_UP)){ absolutey -= increaseby * delta;}
		if (Keyboard.isKeyDown(Keyboard.KEY_DOWN)){ absolutey += increaseby * delta;}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_Q)){
			xtorotate=absolutex-locationx;
			ytorotate=absolutey-locationy;
	    	rotate+=increaseby*(0.15/0.35)*delta;
        	rotation=true;
        }
		
		while (Keyboard.next()) {
		    if (Keyboard.getEventKeyState()) {
		        if (Keyboard.getEventKey() == Keyboard.KEY_F) {
		        	setDisplayMode(800, 600, !Display.isFullscreen());
		        	//rotate=0;
		        	//rotation=false;
		        }
		        //else if(Keyboard.getEventKey()==Keyboard.KEY_Q){
		        //	rotate+=0.15f*delta;
		       // 	rotation=true;
		        //}
		        else if (Keyboard.getEventKey() == Keyboard.KEY_V){
		        	vsync = !vsync;
		        	Display.setVSyncEnabled(vsync);
		        	//rotate=0;
		        	//rotation=false;
		        }
		        else if(Keyboard.getEventKey()==Keyboard.KEY_RETURN){
		        	rotate=0;
		        	rotation=false;
		        }
		        else if(Keyboard.getEventKey()==Keyboard.KEY_W){
		        	//x=Mouse.getX();
		        	//y=Mouse.getY();
		        	zoomin+=0.3;
		        }
		        else if(Keyboard.getEventKey()==Keyboard.KEY_S){
		        	//x=Mouse.getX();
		        	//y=Mouse.getY();
		        	zoomin-=0.3;
		        	if(zoomin<0.1){
		        		zoomin=0.1;
		        	}
		        }
		        else if(Keyboard.getEventKey()==Keyboard.KEY_ESCAPE){
		        	System.exit(0);
		        }
		        else{
		        	//rotate=0;
		        	//rotation=false;
		        }
		        
		    }
		}
		
		// keep quad on the screen
		if (absolutex < locationx)locationx-=width;
		if (absolutex > locationx+width) locationx+=width;
		if (absolutey < locationy) locationy-=height;
		if (absolutey > locationy+height) locationy+= height;
 
		updateFPS(); // update FPS Counter
	}
 
	/**
	 * Set the display mode to be used 
	 * 
	 * @param width The width of the display required
	 * @param height The height of the display required
	 * @param fullscreen True if we want fullscreen mode
	 */
	public void setDisplayMode(int width, int height, boolean fullscreen) {

		// return if requested DisplayMode is already set
                if ((Display.getDisplayMode().getWidth() == width) && 
			(Display.getDisplayMode().getHeight() == height) && 
			(Display.isFullscreen() == fullscreen)) {
			return;
		}
		
		try {
			DisplayMode targetDisplayMode = null;
			
			if (fullscreen) {
				DisplayMode[] modes = Display.getAvailableDisplayModes();
				int freq = 0;
				
				for (int i=0;i<modes.length;i++) {
					DisplayMode current = modes[i];
					
					if ((current.getWidth() == width) && (current.getHeight() == height)) {
						if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
							if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
								targetDisplayMode = current;
								freq = targetDisplayMode.getFrequency();
							}
						}

						// if we've found a match for bpp and frequence against the 
						// original display mode then it's probably best to go for this one
						// since it's most likely compatible with the monitor
						if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel()) &&
						    (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
							targetDisplayMode = current;
							break;
						}
					}
				}
			} else {
				targetDisplayMode = new DisplayMode(width,height);
			}
			
			if (targetDisplayMode == null) {
				System.out.println("Failed to find value mode: "+width+"x"+height+" fs="+fullscreen);
				return;
			}

			Display.setDisplayMode(targetDisplayMode);
			Display.setFullscreen(fullscreen);
			
		} catch (LWJGLException e) {
			System.out.println("Unable to setup mode "+width+"x"+height+" fullscreen="+fullscreen + e);
		}
	}
	
	/** 
	 * Calculate how many milliseconds have passed 
	 * since last frame.
	 * 
	 * @return milliseconds passed since last frame 
	 */
	public int getDelta() {
	    long time = getTime();
	    int delta = (int) (time - lastFrame);
	    lastFrame = time;
 
	    return delta;
	}
 
	/**
	 * Get the accurate system time
	 * 
	 * @return The system time in milliseconds
	 */
	public long getTime() {
	    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
 
	/**
	 * Calculate the FPS and set it in the title bar
	 */
	public void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			Display.setTitle("FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
 
	public void initGL(){
		GL11.glMatrixMode(GL11.GL_PROJECTION);
		GL11.glLoadIdentity();
		GL11.glOrtho(0, 800, 600, 0, 1, -1);
		GL11.glMatrixMode(GL11.GL_MODELVIEW);
	}
 
	public void renderGL(){
		// Clear The Screen And The Depth Buffer
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
 
		// R,G,B,A Set The Color To Blue One Time Only
		GL11.glColor3f(0.75f, 0.75f, 0.75f);
		
		// draw quad
		
			
			//GL11.glTranslatef(x, y, 0);
			//GL11.glRotatef(rotation, 0f, 0f, 1f);
			//GL11.glTranslatef(-x, -y, 0);
		//double x=absolutex-locationx;
		//double y=absolutey-locationy;
		//GL11.glPushMatrix();
		//GL11.glBegin(GL11.GL_QUADS);	
		//	GL11.glVertex2d(x,y);
		//	GL11.glVertex2d(x + 100, y );
		//	GL11.glVertex2d(x + 100, y + 100);
		//	GL11.glVertex2d(x , y + 100);
		//GL11.glEnd();
		//GL11.glPushMatrix();
		//GL11.glBegin(GL11.GL_QUADS);	
		
		double skipby=50;
			int tol=200;
			
			for(int i=0-tol;i<(width+tol)*zoomin;i+=skipby/zoomin){
				for(int j=0-tol;j<(height+tol)*zoomin;j+=skipby/zoomin){
					//double colorred=(255/100)*hash2(MD5("red"+locationx+locationy+i+j));
					//double colorgreen=(255/100)*hash2(MD5("green"+locationx+locationy+i+j));
					//double colorblue=(255/100)*hash2(MD5("blue"+locationx+locationy+i+j));
					
					double z = z(locationx+i,locationy+j);
					//z=hash2(MD5(z+""))/100;
					double param=255;
					double colorred=fred(z)/param;
					double colorgreen=fgreen(z)/param;
					double colorblue=fblue(z)/param;
					
					//colorred=hash2(MD5(colorred+""))/100;
					//colorgreen=hash2(MD5(colorgreen+""))/100;
					//colorblue=hash2(MD5(colorblue+""))/100;
					//System.out.println(colorred+" "+colorgreen+" "+colorblue);
					//0.5f=?
					//GL11.glColo
				
					//System.out.println(((double)(hash2(MD5(colorred+colorgreen+colorblue+"")))/100));
					/*if(((double)(hash2(MD5(colorred+colorgreen+colorblue+"")))/100)>0.4){
						GL11.glColor3d(51/255,0,102/255);
						GL11.glPushMatrix();
						GL11.glBegin(GL11.GL_QUADS);	
							GL11.glVertex2f(i+10, j+10);
							GL11.glVertex2f(i+10+10, j+10);
							GL11.glVertex2f(i+10+10, j+10+10);
							GL11.glVertex2f(i+10, j+10+10);
						GL11.glEnd();
						GL11.glBegin(GL11.GL_TRIANGLES);
							GL11.glVertex2f(i+10, j+10);
							GL11.glVertex2f(i+10+5, j);
							GL11.glVertex2f(i+10+10, j+10);
						GL11.glEnd();
						GL11.glPopMatrix();
					}*/
					
					GL11.glColor3d(colorred, colorgreen, colorblue);
					GL11.glPushMatrix();
					double x1,y1,x2,y2,x3,y3,x4,y4;
					if(rotation){
					GL11.glTranslatef(x, y, 0);
					GL11.glRotatef(rotate, 0f, 0f, 1f);
					GL11.glTranslatef(-x, -y, 0);
					
					/*
					 x1=(i-x)*Math.cos(rotation)-(j-y)*Math.sin(rotation)+x;
					 y1=(i-x)*Math.sin(rotation)+(j-y)*Math.cos(rotation)+y;
					  
					  
					  
					  
					 */
					}
					
					GL11.glBegin(GL11.GL_QUADS);
					//double x=absolutex-locationx;
					//double y=absolutey-locationy;
					GL11.glVertex2d(i,j);
					GL11.glVertex2d(i + skipby,j);
					GL11.glVertex2d(i + skipby,j+skipby);
					GL11.glVertex2d(i,j+skipby);
					//GL11.glVertex3d(i, j,z(i+locationx,j+locationy)*5);
					//GL11.glVertex3d(i+skipby, j,z(i+locationx+skipby,j+locationy)*5);
					//GL11.glVertex3d(i+skipby, j+skipby,z(i+locationx+skipby,j+locationy+skipby)*5);
					//GL11.glVertex3d(i, j+skipby,z(i+locationx,j+locationy+skipby)*5);
					//	GL11.glVertex2d(rotatex(i,j),rotatey(i,j));
					//	GL11.glVertex2d(rotatex(i + skipby,j),rotatey(i,j));
					//	GL11.glVertex2d(rotatex(i + skipby,j+skipby),rotatey(i+skipby,j + skipby));
					//	GL11.glVertex2d(rotatex(i,j+skipby) , rotatey(i,j + skipby));
						//double height=height(locationx+i,locationy+j)*5;
						//GL11.glVertex3d(i, j, height(i,j)*5);
						//height=height(locationx+i,locationy+j+skipby);
						//GL11.glVertex3d(i, j+skipby, height(i,j+skipby)*5);
						//height=height(locationx+i+skipby,locationy+j+skipby);
						//GL11.glVertex3d(i+skipby, j+skipby, height(i+skipby,j+skipby)*5);
						//height=height(locationx+i,locationy+j+skipby);
						//sGL11.glVertex3d(i, j+skipby, height(i,j+skipby)*5);
						//System.out.println(height(i,j)*5+" "+height(i,j+skipby)*5+" "+height(i+skipby,j+skipby)*5+" "+height(i,j+skipby)*5);
					GL11.glEnd();
					GL11.glPopMatrix();
					if(hash2(MD5("red"+locationx+locationy+i+j))>90){
						drawShip(i,j);
					}
				}
				

			}
			GL11.glColor3f(1, 1, 1);
			GL11.glPushMatrix();
			GL11.glBegin(GL11.GL_QUADS);	
				GL11.glColor3d(0,0,0);
				double x=absolutex-locationx;
				double y=absolutey-locationy;
				
				GL11.glVertex2d(x, y);
				GL11.glVertex2d(x+10, y);
				GL11.glVertex2d(x+10, y+10);
				GL11.glVertex2d(x, y+10);
			GL11.glEnd();
			GL11.glPopMatrix();
			
			
			//GL11.glEnd();
			//GL11.glPopMatrix();
			//repaint();
	}
	private double rotatex(double i,double j){
		return rotation?(i-xtorotate)*Math.cos(rotate*Math.PI/180)-(j-ytorotate)*Math.sin(rotate*Math.PI/180)+xtorotate:i;
	}
	private double rotatey(double i,double j){
		return rotation?(i-xtorotate)*Math.sin(rotate*Math.PI/180)+(j-ytorotate)*Math.cos(rotate*Math.PI/180)+ytorotate:j;
	}
	
	
	private void drawShip(int x,int y) {
		GL11.glColor3d(colorred, colorgreen, colorblue);
		GL11.glPushMatrix();
		if(rotation){
		GL11.glTranslatef(x, y, 0);
		GL11.glRotatef(rotate, 0f, 0f, 1f);
		GL11.glTranslatef(-x, -y, 0);}
		
		GL11.glBegin(GL11.GL_QUADS);	
			GL11.glVertex2d(x+25,y+25);
			GL11.glVertex2d(x+25 +10, y+25 );
			GL11.glVertex2d(x+25 + 10, y+25 + 10);
			GL11.glVertex2d(x+25 , y+25 + 10);
		GL11.glEnd();
		
		GL11.glBegin(GL11.GL_LINE);
			//GL11.glVertex2d(x+25+2,y+25+10);
			//GL11.glVertex2d(x+25+2,y+25+10+5);
			//GL11.glVertex2d(x+25,y+25);
			//GL11.glVertex2d(x+25,y+25);
			
		GL11.glEnd();
		GL11.glPopMatrix();
		
	}

	public double height(double x,double y){
		return Math.cos((x)/25)+4*Math.cos((x)/27)+Math.sin((y)/20)+3*Math.sin((y)/25);
	}
	
	public double z(double x,double y){
		/*double length=Math.tan(x);
		double width=Math.sin(y);
		length/=Math.sqrt(length*length+width+width);
		width/=Math.sqrt(length*length+width+width);
		
		return Math.sqrt(length*length+width+width);*/
		//plot cos(x/5)+3cos(y/10)+4sin(x/25)+sin(y/100);
		return Math.cos(x)*Math.sin(y);
		
	}
	public double fred(double z){
		return -371/2*z*z+139/2*z+255;
	}
	public double fgreen(double z){
		return -169*z*z+16*z+222;
	}
	public double fblue(double z){
		return -36*z*z-118*z+173; 
	}
	//-1: 0,37,255
	//0: 255,222,173
	//1: 139, 069, 019
	
	public static void main(String[] argv){
		Alpha1Opengl11v2d fullscreenExample = new Alpha1Opengl11v2d();
		fullscreenExample.start();
		//JFrame f=new JFrame();
		//f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//f.setVisible(true);
		//f.setSize(800, 600);
		//f.add(fullscreenExample);
		//System.out.println(MD5("a"));
		//System.out.println(hash2(MD5("fff")));
	}


	public static String MD5(String md5) { 
		try {       
			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");  
			byte[] array = md.digest(md5.getBytes());  
			StringBuffer sb = new StringBuffer();  
			for (int i = 0; i < array.length; ++i) {   
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));      
				}
			//int d=hashToInt(sb.toString());
			//System.out.println(d);
			//System.out.println(percentile(d));w
			return sb.toString(); 
				} catch (java.security.NoSuchAlgorithmException e) {   
					e.printStackTrace();
				}   
		return null; 
	}
	
	public static int hashToInt(String md5){
		int sum=0;
		//System.out.println(md5+"hashToInt");
		//System.out.println("char "+"value "+"pos  "+"radix pox      "+"equals    "+" sum  ");
		for(int i=31;i>-1;i=i-1){
			//System.out.println(md5.charAt(i)+"      "+charToInt(md5.charAt(i))+"    "+i+"    "+(int)(Math.pow(16,32-i))+"   "+(int)(charToInt(md5.charAt(i))*Math.pow(16,32-i))+"   "+sum);
			//System.out.print("char: "+charToInt(md5.charAt(i))+" ");
			sum+=(int)(charToInt(md5.charAt(i))*(int)(Math.pow(16,32-i)));
		}
		//System.out.println("hash of:"+md5+" is :"+sum);
		return sum;
	}
	public static int charToInt(char a1){
		//System.out.println("charOtint:"+a1);
		switch(a1){
			case '0':return 0;
			case '1':return 1;
			case '2':return 2;
			case '3':return 3;
			case '4':return 4;
			case '5':return 5;
			case '6':return 6;
			case '7':return 7;
			case '8':return 8;
			case '9':return 9;
			case 'a':return 10;
			case 'b':return 11;
			case 'c':return 12;
			case 'd':return 13;
			case 'e':return 14;
			case 'f':return 15;
			
		}
		return 0;
		
	}

	public static int percentile(int c){
		while(c>50){
			c=c/10;
		}
		while(c<-50){
			c=c/10;
		}
		return c+50;	
	}
	
	public static int hash1(String d){
		return hashToInt(d);
	}
	
	public static int hash2(String d){
		return percentile(hash1(d));
	}
	class MinecraftSheep{
		int x=0,y=0;
	public MinecraftSheep(){
		this.x=0;
		this.y=0;
	}
	
		
	}
}
