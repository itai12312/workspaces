package paint;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
public class TestMan {
	public static void main (String [] args)
	{
		JButton a=new JButton("h");
		Man b= new Man();
	    JFrame f=new JFrame("Men");
	    f.setSize(600,600);
	    f.add(b, BorderLayout.CENTER);
	    
	    JPanel eastPanel = new JPanel();
		BoxLayout bl = new BoxLayout(eastPanel, BoxLayout.Y_AXIS);
		eastPanel.setAlignmentX(0);
		eastPanel.setPreferredSize(new Dimension(120, 100));
		eastPanel.setBackground(new Color(100, 20, 0));
		JButton[] jb = new JButton[5];
		String[] imageName = { "hat", "glasses", "shirt", "pants", "shose" };
		ImageIcon[] icon = new ImageIcon[5];
		for (int i = 0; i < 5; i++) {
			icon[i] = new ImageIcon("src/" + imageName[i] + ".png");
			jb[i] = new JButton(icon[i]);
			jb[i].addActionListener(b);
			jb[i].setActionCommand(imageName[i]);
			jb[i].setPreferredSize(new Dimension(100, 100));
			jb[i].setMargin(new Insets(10, 10, 10, 10));
			eastPanel.add(jb[i]);
			
			
			
		}
		f.add(eastPanel, BorderLayout.EAST);
		
	      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );  f.setVisible(true);//הפעלת שיטה paintComponent
      
	  
      
	}
	
}
