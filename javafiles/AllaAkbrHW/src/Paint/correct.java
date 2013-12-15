package Paint;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.Point;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
public class correct extends JFrame implements ActionListener{
	static image a;
	static correct	b;
	public static void main(String[]args){
	b=new correct();
		
	}
	public correct(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600,600);
		a=new image();
		getContentPane().add(a,BorderLayout.CENTER);
		JPanel b=new JPanel();
		b.setLayout(new GridLayout(4,1));
		JButton hat=new JButton("0");
		JButton pants=new JButton("1");
		JButton shirt=new JButton("2");
		JButton gloves=new JButton("3");
		b.add(hat);
		b.add(pants);
		b.add(shirt);
		b.add(gloves);
		hat.addActionListener(this);
		pants.addActionListener(this);
		shirt.addActionListener(this);
		gloves.addActionListener(this);		
		getContentPane().add(b,BorderLayout.EAST);
		setVisible(true);
	}
//public void printComponents(Graphics arg0){	super.printComponents(arg0);}
class image extends JPanel{
	boolean[]zz={false,false,false,false};
	Point []b={new Point(50,50),new Point(100,100),new Point(150,150),new Point(200,200)};
	public image(){
		super();	
	}


	public void paintComponent(Graphics arg0) {
		super.paintComponent(arg0);
		arg0.drawOval(200,200,100,100);
		arg0.drawLine(300,400,300,600);
		arg0.drawLine(300,600,400,700);
		arg0.drawLine(300,600,200,700);
		arg0.drawLine(200,500,500,500);
		for (int i = 0; i < a.zz.length; i++) {
			if(a.zz[i]){
				ImageIcon im=new ImageIcon(i+".gif");
				im.paintIcon(this, arg0, (int)b[i].getX(), (int)b[i].getY());
			}
		}
	}
	public Dimension getPrefferedSize(){
		return new Dimension(500,600);
	}


}

public void actionPerformed(ActionEvent arg0) {
Object help=arg0.getSource();
JButton e=(JButton)help;
a.zz[Integer.parseInt(e.getActionCommand())]=!a.zz[Integer.parseInt(e.getActionCommand())];
a.repaint();
}

}