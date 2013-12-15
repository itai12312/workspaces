package paint;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

class Cloth extends JPanel{
	private Baby a;
	 private int x;
	  private int y;
	public Cloth(String img, int x, int y){
        super();// הרצת בנאי של JPanel
		a=new Baby(img);
		this.x=x;
		this.y=y;
		this.setBackground(new Color(255,100,100));	
	}
	public void paintComponent(Graphics g){
		super.paintComponent(g);// הרצת שיטה של JPanel
		a.draw(this, g, x ,y);
	}
	public Dimension getPrefferedSize(){
		return new Dimension(300,300);
	}
class Baby{
  private String img;
  private boolean paint=false;
  public Baby(String p){
   img=p;
  }
  public void draw(Component f ,Graphics g, int x, int y){
	  if(paint){
      ImageIcon ic=new ImageIcon(img);   // paint picture
	  ic.paintIcon(f,g,x,y);
	  }
  }
}
}