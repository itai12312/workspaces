import javax.swing.*;
public class TestBaby {
	public static void main (String [] args)
	{
		MaatafaBaby msh=new MaatafaBaby("Bar","1.gif",0,0);

	    JFrame f=new JFrame("Baby");
	    f.setSize(300,300);
	    f.add(msh);
	      f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE );;  f.setVisible(true);//הפעלת שיטה paintComponent
      
	  
      
	}
	
}
