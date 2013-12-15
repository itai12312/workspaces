package glmodel;



import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;

/**
 * NeHe Lesson 02 - Your First Polygon
 * 
 * @author Tim Biedert
 * @author kappaOne
 */
public class Lesson02 {

	String windowTitle = "NeHe Lesson 02 - Your First Polygon";
	public boolean closeRequested = false;

	public void run() {
		createWindow();
		
		initGL();
		
		// main loop
		while (!closeRequested) {
			pollInput();
			updateLogic();
			renderGL();
			Display.update();
		}
		
		cleanup();
	}
	
	private void initGL() {
		int width = Display.getDisplayMode().getWidth();
		int height = Display.getDisplayMode().getHeight();

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

	private void updateLogic() {
		
	}
	
	private void renderGL() {

		GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT); // Clear The Screen And The Depth Buffer
		GL11.glLoadIdentity(); // Reset The View

		GL11.glTranslatef(-1.5f, 0.0f, -6.0f); // Move Left 1.5 Units And Into The Screen 6.0

		GL11.glBegin(GL11.GL_TRIANGLES); // Drawing Using Triangles
		GL11.glVertex3f(0.0f, 1.0f, 0.0f); // Top
		GL11.glVertex3f(-1.0f, -1.0f, 0.0f); // Bottom Left
		GL11.glVertex3f(1.0f, -1.0f, 0.0f); // Bottom Right
		GL11.glEnd(); // Finished Drawing The Triangle

		GL11.glTranslatef(3.0f, 0.0f, 0.0f); // Move Right 3 Units

		GL11.glBegin(GL11.GL_QUADS); // Draw A Quad
		GL11.glVertex3f(-1.0f, 1.0f, 0.0f); // Top Left
		GL11.glVertex3f(1.0f, 1.0f, 0.0f); // Top Right
		GL11.glVertex3f(1.0f, -1.0f, 0.0f); // Bottom Right
		GL11.glVertex3f(-1.0f, -1.0f, 0.0f); // Bottom Left
		GL11.glEnd(); // Done Drawing The Quad
	}

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

	private void createWindow() {
		try {
			Display.setDisplayMode(new DisplayMode(640, 480));
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
	
	public static void main(String[] args) {
		Lesson02 lesson = new Lesson02();
		lesson.run();
	}
}
