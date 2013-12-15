import java.awt.Color;
import java.awt.Graphics;
import javax.swing.JComponent;
public class WorldViewer extends JComponent {
	private Game game;
	public WorldViewer(Game gm) {
		game = gm;
	}
	protected void paintComponent(Graphics g) {
		for (Tank tank : game.tanks) {
			g.setColor(Color.GREEN);
			g.fillOval((int)tank.position.x-5,(int)tank.position.y-5,10,10);
			g.setColor(Color.BLUE);
			g.fillArc((int)tank.position.x-5, (int)tank.position.y-5, 10, 10, (int)(tank.sightAngle*180/Math.PI-5), 10);
		}
	}
}