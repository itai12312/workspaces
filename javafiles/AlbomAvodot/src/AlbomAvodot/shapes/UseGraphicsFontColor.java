package AlbomAvodot.shapes;
import java.awt.*;
import javax.swing.*;

import javax.swing.JFrame;

public class UseGraphicsFontColor extends JPanel {
    public UseGraphicsFontColor(){
    	JFrame fr=new JFrame();
		fr.add(this);
		fr.setSize(700,700);
		fr.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		fr.setVisible(true);
    }
	public void paintComponent(Graphics gr){
		super.paintComponent(gr);
	  // Here you have invoke the Graphcs methods.
   	// Use Ctrl+Space after "gr." for help	
	// The paint area is: width:700, height:700
		
		gr.setColor(Color.red);
		gr.drawRect(150, 150, 400, 400);
		gr.setColor(Color.green);
        gr.drawOval(300, 300, 100, 100);		
		
		
		
		
		
     
    }


	public Dimension getPreferredSize(){
		return new Dimension(700,700);
	}
	public static void main(String [] args){
		UseGraphicsFontColor u=new UseGraphicsFontColor();
	
	}
}
