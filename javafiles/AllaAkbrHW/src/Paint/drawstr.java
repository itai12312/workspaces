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
import javax.swing.JTextField;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
public class drawstr extends JFrame implements ActionListener{
	static image a;
	static drawstr	b;
	JTextField a1=new JTextField("day");
	JTextField a2=new JTextField("month");
	JTextField a3=new JTextField("year");

	public static void main(String[]args){
		b=new drawstr();
	}
	
	public drawstr(){
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600,600);
		a=new image();
		getContentPane().add(a,BorderLayout.CENTER);
		JPanel b=new JPanel();
		b.setLayout(new GridLayout(4,1));
		b.add(a1);
		b.add(a2);
		b.add(a3);
		JButton enter=new JButton();
		enter.addActionListener(this);
		b.add(enter);
		getContentPane().add(b,BorderLayout.EAST);
		setVisible(true);
	}
	//public void printComponents(Graphics arg0){	super.printComponents(arg0);}
	private class image extends JPanel{
		String str="";
		ImageIcon icon=null;
		public image(){
			super();	
		}
		
		public void paintComponent(Graphics arg0) {
			super.paintComponent(arg0);
			arg0.drawString(this.str, 100, 100);
			if(icon!=null){
				icon.paintIcon(this, arg0,150,150);
			}
		}
		
		public Dimension getPrefferedSize(){
			return new Dimension(600,600);
		}

	}

	public void actionPerformed(ActionEvent arg0) {
		Object help=arg0.getSource();
		if(help instanceof JButton){
			a.str=a1.getText()+a2.getText()+a3.getText();
			a.icon=new ImageIcon(a1.getText()+".gif");
			a.repaint();
		}
	}

}
