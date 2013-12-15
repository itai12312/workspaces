package Paint;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;
public class Man extends JPanel implements ActionListener{
	private int x;
	private int y;
	private boolean hat,glasses, shirt, pants, shose,shose1;

	public Man(){
		super();// הרצת בנאי של JPanel
		this.setBackground(new Color(255,255,255));	
	}

	public void draw(Component f ,Graphics g){
		Font font=new Font(Font.SANS_SERIF, Font.BOLD, 40);
		g.setFont(font);
		g.setColor(Color.black);
		g.drawString("איש יפה",205,100); 
		g.drawOval(207,121,120,100);
		g.drawLine(267,220, 260, 383);
		g.drawLine(193, 484,260, 383);
		g.drawLine(331, 477,260, 383);
		g.drawLine(144, 297,408,297);


	}

	public void paintComponent(Graphics g){
		super.paintComponent(g);// הרצת שיטה של JPanel
		draw(this, g);
		if(hat){
			ImageIcon img=new ImageIcon("src/hat.png");
			img.paintIcon(this, g,210,90);

		}
		if(glasses){
			ImageIcon img=new ImageIcon("src/glasses.png");
			img.paintIcon(this, g,210,165);

		}
		if(shirt){
			ImageIcon img=new ImageIcon("src/shirt.png");
			img.paintIcon(this, g,170,210);

		}
		if(pants){
			ImageIcon img=new ImageIcon("src/pants.png");
			img.paintIcon(this, g,170,350);

		}
		if(shose&&shose1){
			ImageIcon img=new ImageIcon("src/shose.png");
			img.paintIcon(this, g,190,505);
			ImageIcon img1=new ImageIcon("src/shose1.png");
			img1.paintIcon(this, g,275,505);
		}
	}

	public Dimension getPrefferedSize(){
		return new Dimension(600,600);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		Object ob=e.getSource();
		JButton but=(JButton)ob;
		String s=but.getActionCommand();
		if(s.equals("hat"))
		{
			hat=!hat;
		}
		if(s.equals("glasses"))
		{
			glasses=!glasses;
		}
		if(s.equals("shirt"))
		{
			shirt=!shirt;
		}
		if(s.equals("pants"))
		{
			pants=!pants;
		}
		if(s.equals("shose"))
		{
			shose=!shose;
			shose1=!shose1;
		}
		repaint();
	}

}
