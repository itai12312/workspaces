  package AlbomAvodot;    

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;


import AlbomAvodot.shapes.*;
public class Tafrit  implements ActionListener {


	private JPanel p, controlPanel;
	private JFrame f;
 
	public Tafrit(){
        	f =new JFrame("Albom");
		JMenuBar menuBar;
		f.setSize(600,600);
       		p=new JPanel();
        	f.add(p);
		menuBar = new JMenuBar();
		JMenu menu = new JMenu("A Menu");
		//---------------------------------------------
        JMenuItem menuItem = new JMenuItem("PictLuah");
		menuItem.addActionListener(this);
		menu.add(menuItem);
		//---------------------------------------------
		 menuItem = new JMenuItem("Mouse");
		 menuItem.addActionListener(this);
		 menu.add(menuItem);
		//---------------------------------------------
		 menuItem = new JMenuItem("UseGraphicsFontColor");
		 menuItem.addActionListener(this);
		 menu.add(menuItem);
		//---------------------------------------------
		 menuItem = new JMenuItem("DrawOval");
		 menuItem.addActionListener(this);
		 menu.add(menuItem);
		menuBar.add(menu);
		f.setJMenuBar(menuBar);
		f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        	f.setVisible(true);
	}
	public static void main(String[] args) {
		
		Tafrit a=new Tafrit();
	}
	public void actionPerformed(ActionEvent e) {
		JMenuItem h=(JMenuItem)e.getSource();
		String s=h.getText();
		if(s.equals("DrawOval"))                                // In separated frame. JFrame in constructor
		{
			MouseIgul app=new MouseIgul();
		}
		else
			if(s.equals("UseGraphicsFontColor"))                                // In separated frame. JFrame in constructor
			{
				UseGraphicsFontColor app=new UseGraphicsFontColor();
			}
			else
					{
						f.remove(p);
				                if(controlPanel!=null){
				                  f.remove(controlPanel);
				                  controlPanel=null;
				                }
						if(s.equals("PictLuah"))                                // In menu frame
						{
							PictLuah app=new PictLuah();
							  p=app;
				              //controlPanel=app.getControlPanel();
							  f.add(p);
				              //f.add(controlPanel, BorderLayout.NORTH);
							  f.setSize(500,500);
				             f.setTitle("PictLuah");                        
							  f.getContentPane().doLayout();
							  
							 
						}
						if(s.equals("Mouse"))                                  // In menu frame
						{
							  Mouse app=new Mouse();
							  p=app;
				              //controlPanel=app.getControlPanel();
							  f.add(p);
				              //f.add(controlPanel, BorderLayout.NORTH);
							  f.setSize(400,400);
				              f.setTitle("Mouse");                        
							  f.getContentPane().doLayout();
							  
							 
						}
					}
				
		
	}
	
	
}
