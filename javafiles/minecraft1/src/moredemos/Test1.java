package moredemos;
import java.nio.FloatBuffer;

import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
/**
 * NeHe Lesson 05 - 3D Shapes 
 * @author Tim Biedert
 * @author kappaOne
 */
public class Test1{

	/** time at last frame */
	//long lastFrame=lastFrameTime;
	int length=1000,width=800;
	/** frames per second */
	int fps;
	/** last fps time */
	long lastFPS;
	
	/** is VSync Enabled */
	boolean vsync;
	String windowTitle = " basic 3D Shapes תלת מימד";
	public boolean closeRequested = false;

	long lastFrameTime; // used to calculate delta
	
	//cube stuff
	
	
	//convert all to float(openGL likes them)
	float x=640/2,y=475,z=-25;
	
	boolean stayonslope=true;
	
	/*double[]translate1={-1.5,0.0,-6.0};
	double[]rotate1={0.0,0.0,0.0};
	Trans[]trans1={new Trans(translate1,"translate"),new Trans(rotate1,"rotate")};
	double[][][]coordinates1={
	{{1.0,1.0,-1.0},{-1.0,1.0,-1.0},{-1.0,1.0,1.0},{1.0,1.0,1.0}},
	{{1.0,-1.0,1.0},{-1.0,-1.0,1.0},{-1.0,-1.0,-1.0},{1.0,-1.0,-1.0}},
	{{1.0,1.0,1.0},{-1.0,1.0,1.0},{-1.0,-1.0,1.0},{1.0,-1.0,1.0}},
	{{1.0,-1.0,-1.0},{-1.0,-1.0,-1.0},{-1.0,1.0,-1.0},{1.0,1.0,-1.0}},
	{{-1.0,1.0,1.0},{-1.0,1.0,-1.0},{-1.0,-1.0,-1.0},{-1.0,-1.0,1.0}},
	{{1.0,1.0,-1.0},{1.0,1.0,1.0},{1.0,-1.0,1.0},{1.0,-1.0,-1.0}}};
	double[][][] colorpoints1={
			{{0.0,1.0,0.0}},
			{{1.0,0.5,0.0}},
			{{1.0,0.0,0.0}},
			{{1.0,1.0,0.0}},
			{{0.0,0.0,1.0}},
			{{1.0,0.0,1.0}}};
	
	Shape1 cube=new Shape1(0.0, trans1,"quad", coordinates1, colorpoints1);
	//triangle stuff
	
	double[]translate2={1.5,0.0,-7.0};
	double[]rotate2={0.0,0.0,0.0};
	Trans[]trans2={new Trans(translate2,"translate"),new Trans(rotate2,"rotate")};
	double[][][]coordinates2={
	{{0.0,1.0,0.0},{-1.0,-1.0,1.0},{1.0,-1.0,1.0}},
	{{0.0,1.0,0.0},{-1.0,-1.0,1.0},{1.0,-1.0,-1.0}},
	{{0.0,1.0,0.0},{1.0,-1.0,-1.0},{-1.0,-1.0,-1.0}},
	{{0.0,1.0,0.0},{-1.0,-1.0,-1.0},{-1.0,-1.0,1.0}},};
	double[][][] colorpoints2={
	{{1.0,0.0,0.0},{0.0,1.0,0.0},{0.0,0.0,1.0}},
	{{1.0,0.0,0.0},{0.0,0.0,1.0},{0.0,1.0,0.0}},
	{{1.0,0.0,0.0},{0.0,1.0,0.0},{0.0,0.0,1.0}},
	{{1.0,0.0,0.0},{0.0,0.0,1.0},{0.0,1.0,0.0}},};
	Shape1 pyramid=new Shape1(0.0d,trans2,"triangle",coordinates2,colorpoints2);
	
	Shape1[]shapes={cube,pyramid};
	*/
	public void run(){

		createWindow();
		getDelta(); // Initialise delta timer
		initGL();
		lastFPS=getTime();
		renderGL();
		while (!closeRequested) {
			pollInput();
			updateLogic(getDelta());
			//renderGL();

			Display.update();
		}
		
		cleanup();
	}
	
	private void initGL() {

		/* OpenGL */
		int width = Display.getDisplayMode().getWidth();
		int height = Display.getDisplayMode().getHeight();
		System.out.println(width+" "+height);
		GL11.glViewport(0, 0, width, height); // Reset The Current Viewport
		GL11.glMatrixMode(GL11.GL_PROJECTION); // Select The Projection Matrix
		GL11.glLoadIdentity(); // Reset The Projection Matrix
		GLU.gluPerspective(45.0f, ((float) width / (float) height), 0.1f, 100.0f); // Calculate The Aspect Ratio Of The Window
		GL11.glMatrixMode(GL11.GL_MODELVIEW); // Select The Modelview Matrix
		GL11.glLoadIdentity(); // Reset The Modelview Matrix

		GL11.glShadeModel(GL11.GL_SMOOTH); // Enables Smooth Shading
		GL11.glClearColor(0.0f, 0.0f, 0.0f, 0.0f); // Black Background
		GL11.glClearDepth(1.0f); // Depth Buffer Setup
		GL11.glEnable(GL11.GL_DEPTH_TEST); // Enables Depth Testing
		GL11.glDepthFunc(GL11.GL_LEQUAL); // The Type Of Depth Test To Do
		GL11.glHint(GL11.GL_PERSPECTIVE_CORRECTION_HINT, GL11.GL_NICEST); // Really Nice Perspective Calculations

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
		//return -100-300*Math.abs(Math.cos(x)*Math.sin(y));
		return -10+10*Math.abs(Math.cos(x)*Math.sin(y));
	}
	
	public double fred(double x){
		return (111*x*x-222*x+200)/256;
	}
	public double fgreen(double x){
		return (-180*x*x+360*x+20)/256;
	}
	public double fblue(double x){
		return (160*x*x-370*x*x+250)/256;
	}
	
	private void updateLogic(int delta) {
		//triangleAngle += 0.1f * delta; // Increase The Rotation Variable For The Triangles
		//quadAngle -= 0.05d * delta; // Decrease The Rotation Variable For The Quads
		double k=0.05*0.1;
		double rotaterate=0.5;
	/*
		if(Keyboard.isKeyDown(Keyboard.KEY_Z)){
			shapes[0].shapeAngle+=0.1d*delta*rotaterate;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_X)){
			shapes[0].shapeAngle-=0.1d*delta*rotaterate;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_C)){
			shapes[1].shapeAngle+=0.1d*delta*rotaterate;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_V)){
			shapes[1].shapeAngle-=0.1d*delta*rotaterate;
		}
	*/
		//System.out.println("hi1");
		double rate=4000*5;
		if(Keyboard.isKeyDown(Keyboard.KEY_A)){
			x-=0.05d*k*rate;
			renderGL();
		}		
		if(Keyboard.isKeyDown(Keyboard.KEY_D)){
			x+=0.05d*k*rate;
			renderGL();
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_W)){
			y+=0.05d*k*rate;
			renderGL();
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_S)){
			y-=0.05d*k*rate;
			renderGL();
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_Q)){
			z+=0.05d*k*rate;
			renderGL();
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_E)){
			z-=0.05d*k*rate;
			renderGL();
		}
		
		/*
		if(Keyboard.isKeyDown(Keyboard.KEY_LEFT)){
			shapes[1].trans[0].trans[0]-=0.05d*k;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_RIGHT)){
			shapes[1].trans[0].trans[0]+=0.05d*k;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_UP)){
			shapes[1].trans[0].trans[1]+=0.05d*k;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_DOWN)){
			shapes[1].trans[0].trans[1]-=0.05d*k;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_G)){
			shapes[1].trans[0].trans[2]-=0.05d*k;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_B)){
			shapes[1].trans[0].trans[2]+=0.05d*k;
		}
		
		if(Keyboard.isKeyDown(Keyboard.KEY_Y)){
			shapes[1].trans[1].trans[0]-=0.05d*k;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_H)){
			shapes[1].trans[1].trans[0]+=0.05d*k;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_U)){
			shapes[1].trans[1].trans[1]+=0.05d*k;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_J)){
			shapes[1].trans[1].trans[1]-=0.05d*k;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_I)){
			shapes[1].trans[1].trans[2]-=0.05d*k;
		}
		if(Keyboard.isKeyDown(Keyboard.KEY_K)){
			shapes[1].trans[1].trans[2]+=0.05d*k;
		}
		*/
		
		//while (Keyboard.next()) {
		  //  if (Keyboard.getEventKeyState()) {
		        if (Keyboard.getEventKey() == Keyboard.KEY_F) {
		        	setDisplayMode(800, 600, !Display.isFullscreen());
		        }
		        else if (Keyboard.getEventKey() == Keyboard.KEY_R) {
		        	vsync = !vsync;
		        	Display.setVSyncEnabled(vsync);
		        }
		   // }
		//}
		//System.out.println("hi");
		//shapes[0].print();shapes[1].print();
		        
		        
		//if(Mouse.getX()){}
		        
		        
		updateFPS();
	}

	public void updateFPS() {
		if (getTime() - lastFPS > 1000) {
			Display.setTitle("basic FPS: " + fps);
			fps = 0;
			lastFPS += 1000;
		}
		fps++;
	}
	private void renderGL() {
		double scale=5.0;
		//float[] coord_arr = new float[480000*3*4];
		//float[] color_arr = new float[480000*12];
		//System.out.println(GL11.glGetString(GL11.GL_VERSION));
		System.out.println(x+" "+y+" "+z);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer
		//GL11.glColor3d(0.0, 1.0, 0.5);
		GL11.glLoadIdentity();//reset view
		
		GL11.glBegin(GL11.GL_QUADS);
		//GL_QUAD_STRIP-2 insteead of 4 
		for(int t=-640;t<640;t++){
			for(int t1=-480;t1<480;t1++){
				double z=height(t, t1);
				GL11.glColor3d(fred(z), fgreen(z), fblue(z));
				//GL11.glColor3d(0.5,(-1)*(-1.0-z(t/scale,t1/scale)/10),0.5);
				
				//if(t==x&&z(t/scale,t1/scale)==y){
				//	GL11.glColor3d(0.6, 0, 0.6);
				//}
				
				//color1:200 20 250
				//color2:89 200 40
				//color2:200 20 150
				
				
				
				GL11.glVertex3d(t, z(t/scale,t1/scale), t1);
				GL11.glVertex3d(t+1, z((t+1)/scale,t1/scale), t1);
				GL11.glVertex3d(t+1, z((t+1)/scale,(t1+1)/scale),t1+1 );
				GL11.glVertex3d(t,z(t/scale,(t1+1)/scale), t1+1 );
				
				
				
				/*coord_arr[t1*12+t*12*300]=t;
				coord_arr[t1*12+t*12*300+1]=(float)z(t/scale,t1/scale);
				coord_arr[t1*12+t*12*300+2]=t1;
				
				coord_arr[t1*12+t*12*300+3]=t+1;
				coord_arr[t1*12+t*12*300+4]=(float)z((t+1)/scale,t1/scale);
				coord_arr[t1*12+t*12*300+5]=t1;
				
				coord_arr[t1*12+t*12*300+6]=t+1;
				coord_arr[t1*12+t*12*300+7]=(float)z((t+1)/scale,(t1+1)/scale);
				coord_arr[t1*12+t*12*300+8]=t1+1;
				
				coord_arr[t1*12+t*12*300+9]=t;
				coord_arr[t1*12+t*12*300+10]=(float)z(t/scale,(t1+1)/scale);
				coord_arr[t1*12+t*12*300+11]=t1+1;
				
				for (int vertex=0;vertex<4;vertex++) {
					color_arr[t1*12+t*12*300+vertex*3]=0.0f;
					color_arr[t1*12+t*12*300+vertex*3]=(float)((-1)*(-1.0-z(t/scale,t1/scale)/10));
					color_arr[t1*12+t*12*300+vertex*3]=0.0f;
				}
				*/
			}
		}
		//GL11.glColor3d(0.5, 0.5, 0.5);
		//GL11.glVertex3d(x, z(x/scale,y/scale), y);
		//GL11.glVertex3d(x+0.5, z((x+0.5)/scale,y/scale), y);
		//GL11.glVertex3d(x+0.5, z((x+0.5)/scale,(y+0.5)/scale), y+0.5);
		//GL11.glVertex3d(x, z(x/scale,(y+0.5)/scale), y+0.5);
		
		//FloatBuffer buf =FloatBuffer.wrap(coord_arr);
		//GL11.glVertexPoin
		//GL11.glVertexPointer(size, type, stride, pointer_buffer_offset)  //look @ opengl3 reference for parameters!!
		//GL11.glDrawArrays(GL11.GL_QUADS, 0, 480000);
		//GL11.glColor3f(0.6, 0.6, 0.6);
		GL11.glEnd();
		
		//GL11.glLoadIdentity();
		GL11.glPushMatrix();
		GL11.glBegin(GL11.GL_QUADS);
		GL11.glColor3d(0.5, 0.5, 0.5);
		GL11.glVertex3d(0, z(0,-30),-30);
		GL11.glVertex3d(0+2, z(2,-30),-30);
		GL11.glVertex3d(0+2,z(2,-30+2),-30);
		GL11.glVertex3d(0,z(0,-30),-30);
		GL11.glEnd();
		GL11.glPopMatrix();
		/*for(Shape1 shape:shapes){
			GL11.glLoadIdentity();//reset view
		shape.draw();
		}*/
	}

	/**
	 * Poll Input
	 */
	public void pollInput() {
		// scroll through key events
		while (Keyboard.next()) {
			if (Keyboard.getEventKeyState()) {
				if (Keyboard.getEventKey() == Keyboard.KEY_ESCAPE)
					closeRequested = true;
			}
		}

		if (Display.isCloseRequested()) {
			closeRequested = true;
		}
	}
	
	/** 
	 * Calculate how many milliseconds have passed 
	 * since last frame.
	 * 
	 * @return milliseconds passed since last frame 
	 */
	public int getDelta() {
	    long time = (Sys.getTime() * 1000) / Sys.getTimerResolution();
	    int delta = (int) (time - lastFrameTime);
	    lastFrameTime = time;
	 
	    return delta;
	}
	private void createWindow() {
		try {
			Display.setDisplayMode(new DisplayMode(length,width));
			Display.setVSyncEnabled(true);
			Display.setTitle(windowTitle);
			Display.create();
		} catch (LWJGLException e) {
			Sys.alert("Error", "Initialization failed!\n\n" + e.getMessage());
			System.exit(0);
		}
	}	
	/**
	 * Destroy and clean up resources
	 */
	private void cleanup() {
		Display.destroy();
	}
	public long getTime() {
	    return (Sys.getTime() * 1000) / Sys.getTimerResolution();
	}
	public static void main(String[] args) {
		Test1 cubedemo= new Test1();
		cubedemo.run();
		System.out.println("this is demo");
	}
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
	
	
}
