package Mouse;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class Ex1 extends JPanel implements MouseListener,MouseMotionListener{
	  private int x,y,x1,y1;
	  private static final String  str []={"mouseDragged","mousePressed","mouseExited","mouseClicked+changing the coordinates","mouseEntered","mouseReleased","mouseMoved","mouseReleased","mouseMoved"};
	  private int index;
	  private JTextField a=new JTextField("coor");
	  private ImageIcon b=new ImageIcon("si.jpg");
	  private int increasex=0,increasey=0;
	  public Ex1(String name){
	    super();
	    x=250;
	    y=250;
		index=-1;
		a.setEditable(false);
	    this.addMouseListener(this);
	    this.addMouseMotionListener(this);
	    add(a);
	    JFrame fr=new JFrame(name);
	    fr.add(this);
		fr.setSize(500,500);
		setBackground(Color.green);
		fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	    fr.setVisible(true);
		
	  }
	  public void paintComponent(Graphics g)
	  {
		  super.paintComponent(g);
		  if (index!=-1)
		  {
			  
			  a.setText(x+" "+y);
		  	  g.drawString(str[index]+x+" "+y,x,y);
		  	  if(index==5){
		  		  g.drawLine(x+increasex, y+increasey, x1+increasex, y1+increasey);
		  		  g.drawRect(x, y, Math.abs(x1-x), Math.abs(y1-y));
		  	  }
		  	  b.paintIcon(this, g,x, y);
		  	 
		  }
	      index=-1;
	  }
		 public void mouseDragged(MouseEvent e){ //MouseMotionListener
             index=0;
			 repaint();

          }
public void mousePressed(MouseEvent e){  //MouseListener
            index=1;
			repaint();
          }

public void mouseExited(MouseEvent e){  //MouseListener
            index=2;
			repaint();
}
public void mouseClicked(MouseEvent e){  //MouseListener
           index=3;
           x=e.getX();
           y=e.getY();
           
		   repaint();
}
public void mouseEntered(MouseEvent e){  //MouseListener
           index=4;
		   repaint();
}
public void mouseReleased(MouseEvent e){  //MouseListener
           index=5;
           x1=e.getX();
           y1=e.getY();
 		   repaint();
}
public void mouseMoved(MouseEvent e){  //MouseMotionListener
           index=6;
		   repaint();
}



	  public static void main(String[] args)
	  {
	    Ex1 one=new Ex1("My place");

	  }
	
	}



