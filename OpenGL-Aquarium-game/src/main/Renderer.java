package main;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.Queue;

import javax.imageio.ImageIO;
import javax.imageio.ImageReader;

import model.Rectangle;
import model.RenderGroup;

import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.omg.CORBA.PRIVATE_MEMBER;


public class Renderer {
	public static final int COORD3_TEX2 = 0;
	public static final int COORD3_CLR3 = 1;
	public static int coord_pos;
	public static int tex_pos;
	public static int tex_coord_pos;
	public static int color_pos;
	public static int fade_pos;
	public static int win_size_pos;
	public static int cam_rot_pos;
	public static int cam_pos_pos;
	public static BitmapFont font;
	public static double camRotX, camRotY, camRotZ;
	public static double camPosX, camPosY, camPosZ;
	static int curScreen;
	static char ch = 32;
	static Queue<RenderGroup> renderGroups;
	static int[] programs;
	static long frameStart;
	static long frameTime;
	static long gameStart;
	static void drawUI() {
		//addModel(new Rectangle(0.2f,0.1f));
		RenderGroup rg = new RenderGroup();
		rg.add(font.getStringModel("ABCD456"));
		addGroup(rg);
		/*System.out.println("------------"+vertVect.size());
		for (i=0;i < vertVect.size();i++) {
			System.out.print(vertVect.elementAt(i)+" ");
			if (i%3 == 0)
				System.out.println();
		}*/
	}
	public static int getFrameTime() {
		return (int)frameTime;
	}
	public static int getGameTimeMillis() {
		return (int)((System.nanoTime() - gameStart)/1000000);
	}
	public static void addGroup(RenderGroup rg) {
		renderGroups.add(rg);
	}
	public static void render() {
		renderPlane(true);
		GL11.glClearDepth(10000000.0);
		GL11.glClear(GL11.GL_DEPTH_BUFFER_BIT);
		renderPlane(false);
		renderGroups = new LinkedList<RenderGroup>();
		Display.update();
		frameTime = System.nanoTime() - frameStart;
		frameStart = System.nanoTime();

	}
	private static void renderPlane(boolean isWorld) {
		RenderGroup queueEnd = new RenderGroup();
		RenderGroup curGroup;
		renderGroups.add(queueEnd);
		
		coord_pos = GL20.glGetAttribLocation(programs[COORD3_TEX2], "pos");
		fade_pos = GL20.glGetUniformLocation(programs[COORD3_TEX2], "fade");
		win_size_pos = GL20.glGetUniformLocation(programs[COORD3_TEX2], "win_size");
		tex_coord_pos = GL20.glGetAttribLocation(programs[COORD3_TEX2], "tex_coord");
		tex_pos = GL20.glGetUniformLocation(programs[COORD3_TEX2], "tex");
		cam_rot_pos = GL20.glGetUniformLocation(programs[COORD3_TEX2], "cam_rot");
		cam_pos_pos = GL20.glGetUniformLocation(programs[COORD3_TEX2], "cam_pos");
		GL20.glUseProgram(programs[COORD3_TEX2]);
		GL20.glUniform2i(win_size_pos, Display.getWidth(), Display.getHeight());
		while (renderGroups.peek() != queueEnd) {
			curGroup = renderGroups.poll();
			if (curGroup.program == COORD3_TEX2 && curGroup.inWorld == isWorld)
				curGroup.render();
			else
				renderGroups.add(curGroup);
		}
		renderGroups.add(renderGroups.poll());
		
		coord_pos = GL20.glGetAttribLocation(programs[COORD3_CLR3], "pos");
		color_pos = GL20.glGetAttribLocation(programs[COORD3_CLR3], "color");
		fade_pos = GL20.glGetUniformLocation(programs[COORD3_CLR3], "fade");
		win_size_pos = GL20.glGetUniformLocation(programs[COORD3_CLR3], "win_size");
		cam_rot_pos = GL20.glGetUniformLocation(programs[COORD3_CLR3], "cam_rot");
		cam_pos_pos = GL20.glGetUniformLocation(programs[COORD3_CLR3], "cam_pos");
		GL20.glUseProgram(programs[COORD3_CLR3]);
		GL20.glUniform2i(win_size_pos, Display.getWidth(), Display.getHeight());
		while (renderGroups.peek() != queueEnd) {
			curGroup = renderGroups.poll();
			if (curGroup.program == COORD3_CLR3 && curGroup.inWorld == isWorld)
				curGroup.render();
			else
				renderGroups.add(curGroup);
		}
		renderGroups.add(renderGroups.poll());
	}
	static int shaderFromFile(int shaderType, String path) throws IOException {
		int shader;
		byte[] shaderBytes = new byte[10000];
		InputStream shaderStream = Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
		shaderStream.read(shaderBytes);
		String shaderString = new String(shaderBytes);
		shader = GL20.glCreateShader(shaderType);
		GL20.glShaderSource(shader, shaderString);
		GL20.glCompileShader(shader);
		System.out.print("shader log:\n"+GL20.glGetShaderInfoLog(shader, 10000));
		return shader;
	}
	public static void reset() {
		frameStart = System.nanoTime();
		gameStart = System.nanoTime();
		camPosX = 0.0;
		camPosY = 0.0;
		camPosZ = 0.0;
		camRotX = 0.0;
		camRotY = 0.0;
		camRotZ = 0.0;
	}
	public static void init() throws LWJGLException, IOException, FontFormatException {
		int vertexShader;
		int fragmentShader;
		
		programs = new int[2];
		reset();
		renderGroups = new LinkedList<RenderGroup>();
		InputStream fontFile = Thread.currentThread().getContextClassLoader().getResourceAsStream("font/FreeSans.ttf");
		//init display
		Display.setDisplayMode(new DisplayMode(800,600)); 
		Display.create();
		//compile & link shaders
		programs[COORD3_TEX2] = GL20.glCreateProgram();
		vertexShader = shaderFromFile(GL20.GL_VERTEX_SHADER,"COORD3_TEX2_vert.txt");
		GL20.glAttachShader(programs[COORD3_TEX2], vertexShader);
		GL20.glAttachShader(programs[COORD3_TEX2], shaderFromFile(GL20.GL_FRAGMENT_SHADER,"COORD3_TEX2_frag.txt"));
		GL20.glLinkProgram(programs[COORD3_TEX2]);
		GL20.glUseProgram(programs[COORD3_TEX2]);
		programs[COORD3_CLR3] = GL20.glCreateProgram();
		vertexShader = shaderFromFile(GL20.GL_VERTEX_SHADER,"COORD3_CLR3_vert.txt");
		fragmentShader = shaderFromFile(GL20.GL_FRAGMENT_SHADER,"COORD3_CLR3_frag.txt");
		GL20.glAttachShader(programs[COORD3_CLR3], vertexShader);
		GL20.glAttachShader(programs[COORD3_CLR3], fragmentShader);
		GL20.glLinkProgram(programs[COORD3_CLR3]);
		GL20.glUseProgram(programs[COORD3_CLR3]);
		//create font
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		font = new BitmapFont(Font.createFont(Font.TRUETYPE_FONT,fontFile).deriveFont(20f));
		TexUtils.init();
	}
	public static void clear() {
		RenderGroup bkg = new RenderGroup();
		GL11.glClearColor(0.0f, 1.0f, 1.0f, 0.0f);
		GL11.glClearDepth(10000000.0);
		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT|GL11.GL_DEPTH_BUFFER_BIT);
		bkg.add(new Rectangle(20000.0,20000.0,-10000.0,-10000.0,10000.0));
		bkg.useCam = false;
		Renderer.addGroup(bkg);
	}
}
