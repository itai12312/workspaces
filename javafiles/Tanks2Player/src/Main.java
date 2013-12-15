import javax.swing.JFrame;

public class Main {
	public static void main(String[] args) throws Exception {
		Game game = new Game();
		GameFrame gameFrame = new GameFrame(game);
		while(true) {
			gameFrame.update();
			gameFrame.repaint();
			Thread.sleep(50);
		}
	}
}
