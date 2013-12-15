package sharedscreen;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.util.Timer;
import java.util.TimerTask;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

public class screen extends JFrame{
	 JPopupMenu Pmenu;
	public static void main(String[] args) {
		screen s=new screen();
		s.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		s.setTitle("MoveMessageDemo");
		s.setLocationRelativeTo(null); // Center the frame
		s.setSize(400, 500);
		s.setVisible(true);

	}
	
	public screen(){
		Pmenu = new JPopupMenu();
		  JMenuItem menuItem;
		  Pmenu.add(new JMenuItem("nothing"));
		  Pmenu.add(new JMenuItem("copy"));
		  Pmenu.add(new JMenuItem("past"));
		  Pmenu.add(menuItem=new JMenuItem("delete"));
		
		  addMouseListener(new MouseAdapter(){
				public void mouseReleased(MouseEvent e) {
					if(e.isPopupTrigger()){
						Pmenu.show(e.getComponent(), e.getX(), e.getY());
					}
					//if(e.getModifiers()==InputEvent.BUTTON1_MASK){};//left=button1,center=button2,right=button3, ,
				}
			});	
		A a=new A();
		add(a);
		a.repaint();
		repaint();
		
	}
	public void paint(Graphics g) {
	    g.setColor (Color.yellow);  
	    g.fillOval (5, 15, 50, 75);    
	  }

}
class A extends JPanel{
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		g.drawString("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaa", 30, 30);
		g.drawLine(0,0,100,100);
	}
	
	public A(){
		
	}
	public void draw(){
		repaint();
	}
	
	
}