package opengldemos;
import java.applet.Applet;
import java.awt.Canvas;

public class ExampleApplet extends Applet {

	Canvas display_parent;
	
	public void init() {
		display_parent = new Canvas();
		display_parent.setSize(getWidth(),getHeight());
		add(display_parent);
		display_parent.setFocusable(true);
		display_parent.requestFocus();
		display_parent.setIgnoreRepaint(true);
		setVisible(true);
	}

	public void start() {
		
	}

	public void stop() {
		
	}
	
	
	public void destroy() {
		
	}
}