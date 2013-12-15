package glmodel;



import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;

/**
 * NeHe Lesson 01 - Setting Up An OpenGL Window
 * 
 * @author Tim Biedert
 * @author kappaOne
 */
public class Lesson01 {

	String windowTitle = "NeHe Lesson 01 - Setting Up An OpenGL Window";
	public boolean closeRequested = false;

	/**
	 * Main body, init, loop and clean up
	 */
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
	
	/**
	 * Initialise OpenGL
	 */
	private void initGL() {

	}
	
	/**
	 * Update Logic
	 */
	private void updateLogic() {
		
	}
	
	/**
	 * Render OpenGL
	 */
	private void renderGL() {

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
	 * clean up resources
	 */
	private void cleanup() {
		Display.destroy();
	}

	public static void main(String[] args) {
		Lesson01 lesson = new Lesson01();
		lesson.run();
	}
}
