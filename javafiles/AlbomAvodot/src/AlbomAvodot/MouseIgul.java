  package AlbomAvodot;    
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MouseIgul extends JPanel implements MouseListener,MouseMotionListener{
  int x,y, x1,y1;
  boolean ok;
  public MouseIgul()
  {
    super();
    ok=false;
    addMouseListener(this);
    addMouseMotionListener(this);
	JFrame frame=new JFrame();
	frame.getContentPane().add(this);
	frame.setSize(400,400);
	frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	frame.setVisible(true);
   }
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    if (ok)
    { 
      int xStart, yStart, xEnd, yEnd;
      if (x<x1)
      {
        xStart=x;
        xEnd=x1;
      }
      else
      {
        xStart=x1;
        xEnd=x;
      }
      if (y<y1)
      {
        yStart=y;
        yEnd=y1;
      }
      else
      {
        yStart=y1;
        yEnd=y;
      }
      int width = xEnd - xStart;
      int height = yEnd - yStart;
      g.drawOval(xStart,yStart,width,height);
    }
  }
  public void mouseDragged(MouseEvent e)
              {

                    x1=e.getX();
                    y1=e.getY();
                    repaint();

              }
  public void mousePressed(MouseEvent e)
              {
                 ok=true;
                 x=e.getX();
                 y=e.getY();
              }

  public void mouseExited(MouseEvent e){}
  public void mouseClicked(MouseEvent e){}
  public void mouseEntered(MouseEvent e){}
  public void mouseReleased(MouseEvent e){
	x1=e.getX();
    y1=e.getY();
    repaint();
  }
  public void mouseMoved(MouseEvent e){}
  public static void main(String[] args)
  {
    MouseIgul k=new MouseIgul();
   

  }
}


