  package AlbomAvodot;                                          
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
public class Mouse extends JPanel implements MouseListener,MouseMotionListener{
  private int x,y;
  private static final String  str []={"mouseDragged","mousePressed","mouseExited","mouseClicked+changing the coordinates","mouseEntered","mouseReleased","mouseMoved","mouseReleased","mouseMoved"};
  private int index;

  public Mouse()
  {
    super();
    x=250;
    y=250;
	index=-1;
    this.addMouseListener(this);
    this.addMouseMotionListener(this);
    setBackground(Color.green);
	
  }
  public void paintComponent(Graphics g)
  {
	  super.paintComponent(g);
	  if (index!=-1)
	  {
	  	  g.drawString(str[index],x,y);
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
     		   repaint();
  }
  public void mouseMoved(MouseEvent e){  //MouseMotionListener
	           index=6;
			   repaint();
  }
  public static void main(String[] args)
  {
    Mouse one=new Mouse();
    JFrame fr=new JFrame("My place");
    fr.add(one);
	fr.setSize(500,500);
	fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    fr.setVisible(true);

  }
}


