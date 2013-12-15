package remoteControllreversed;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

public class MoveDemo1 extends JPanel implements KeyListener{


  private Rectangle rect = new Rectangle(0,0,50,50);


  public MoveDemo1(){
    setBackground(Color.red);

    addKeyListener(this);
    addMouseListener(
          new MouseAdapter()
          {
            public void mousePressed(MouseEvent e)
              {requestFocus();
              }
           }
     );
  }


  public void paintComponent(Graphics g){
    super.paintComponent(g);
    g.setColor(Color.white);
    g.fillRect(rect.x, rect.y, rect.width, rect.height);
  }


  public static void main(String [] args){
    JFrame frame = new JFrame("MoveDemo");
    MoveDemo1 demo = new MoveDemo1();
    frame.setBounds(50, 50, 500, 500);
    frame.setLayout(new BorderLayout());
    frame.add(demo);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    frame.setVisible(true);
  }


@Override
public void keyPressed(KeyEvent evt) {
	int keycode = evt.getKeyCode();
    boolean b=evt.isActionKey();
    System.out.println(b+"     "+keycode);
    switch(keycode){
            case KeyEvent.VK_LEFT: rect.x -= 10;System.out.println("KeyEvent.VK_LEFT="+KeyEvent.VK_LEFT); break;
            case KeyEvent.VK_RIGHT: rect.x += 10; break;
            case KeyEvent.VK_UP: rect.y -= 10; break;
            case KeyEvent.VK_DOWN: rect.y += 10; break;
    }

   repaint();
	
}


@Override
public void keyReleased(KeyEvent evt) {
	// TODO Auto-generated method stub
	
}


@Override
public void keyTyped(KeyEvent evt) {
	// TODO Auto-generated method stub
	
}


}