import java.awt.Component;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;


public class GameFrame extends JFrame implements KeyListener {
	Game game;
	private WorldViewer wv;
	
	public GameFrame(Game g) {
		super();
		game = g;
		wv = new WorldViewer(game);
		setSize(800, 600);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		add(wv);
		setVisible(true);
		addKeyListener(this);
	}

	public void keyPressed(KeyEvent arg0) {
		if(arg0.getKeyCode()==KeyEvent.VK_RIGHT) {
			game.tanks[0].setMotion(Direction.RIGHT);
		}
		if(arg0.getKeyCode()==KeyEvent.VK_LEFT) {
			game.tanks[0].setMotion(Direction.LEFT);
		}
		if(arg0.getKeyCode()==KeyEvent.VK_UP) {
			game.tanks[0].setMotion(Direction.UP);
		}
		if(arg0.getKeyCode()==KeyEvent.VK_DOWN) {
			game.tanks[0].setMotion(Direction.DOWN);
		}
		
		
		if(arg0.getKeyCode()==KeyEvent.VK_D) {
			game.tanks[1].setMotion(Direction.RIGHT);
		}
		if(arg0.getKeyCode()==KeyEvent.VK_A) {
			game.tanks[1].setMotion(Direction.LEFT);
		}
		if(arg0.getKeyCode()==KeyEvent.VK_W) {
			game.tanks[1].setMotion(Direction.UP);
		}
		if(arg0.getKeyCode()==KeyEvent.VK_S) {
			game.tanks[1].setMotion(Direction.DOWN);
		}
		
		if(arg0.getKeyCode()==KeyEvent.VK_Q) {
			game.tanks[1].setSightRotation(-0.1);//rotate left
		}
		if(arg0.getKeyCode()==KeyEvent.VK_E) {
			game.tanks[1].setSightRotation(0.1);//rotate right
		}
		if(arg0.getKeyCode()==KeyEvent.VK_PAGE_UP) {
			game.tanks[0].setSightRotation(-0.1);//rotate left
		}
		if(arg0.getKeyCode()==KeyEvent.VK_PAGE_DOWN) {
			game.tanks[0].setSightRotation(0.1);//rotate right
		}
	}

	public void keyReleased(KeyEvent arg0) {
		if(arg0.getKeyCode()==KeyEvent.VK_RIGHT) {
			game.tanks[0].setMotion(Direction.STOP);
		}
		if(arg0.getKeyCode()==KeyEvent.VK_LEFT) {
			game.tanks[0].setMotion(Direction.STOP);
		}
		if(arg0.getKeyCode()==KeyEvent.VK_UP) {
			game.tanks[0].setMotion(Direction.STOP);
		}
		if(arg0.getKeyCode()==KeyEvent.VK_DOWN) {
			game.tanks[0].setMotion(Direction.STOP);
		}
		
		
		if(arg0.getKeyCode()==KeyEvent.VK_D) {
			game.tanks[1].setMotion(Direction.STOP);
		}
		if(arg0.getKeyCode()==KeyEvent.VK_A) {
			game.tanks[1].setMotion(Direction.STOP);
		}
		if(arg0.getKeyCode()==KeyEvent.VK_W) {
			game.tanks[1].setMotion(Direction.STOP);
		}
		if(arg0.getKeyCode()==KeyEvent.VK_S) {
			game.tanks[1].setMotion(Direction.STOP);
		}
		
		if(arg0.getKeyCode()==KeyEvent.VK_Q) {
			game.tanks[1].setSightRotation(0);//stop rotate
		}
		if(arg0.getKeyCode()==KeyEvent.VK_E) {
			game.tanks[1].setSightRotation(0);//stop rotate
		}
		if(arg0.getKeyCode()==KeyEvent.VK_PAGE_UP) {
			game.tanks[0].setSightRotation(0);//stop rotate
		}
		if(arg0.getKeyCode()==KeyEvent.VK_PAGE_DOWN) {
			game.tanks[0].setSightRotation(0);//stop rotate
		}
	}

	public void keyTyped(KeyEvent arg0) {
		
	}

	public void update() {
		game.update();
		requestFocus();
	}
	
}