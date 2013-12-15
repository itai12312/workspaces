package Paint;import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
//move image in darg, 
//move stright line in dag
//paint like program


public class WithMenu extends JPanel implements ActionListener{
	
	private JTextField jf;
	
	private JPanel a1;
	
	public WithMenu(){
		   a1=new JPanel();
		   a1.setBackground(Color.blue);
		      JButton b=new JButton("go");
		       b.addActionListener(this);
		      b.setActionCommand("aa");
		      a1.add(b);
		       jf=new JTextField(20);
		      jf.addActionListener(this);
			   a1.add(jf);
			   
			   
			   
			   
			   JMenuBar menuBar = new JMenuBar();
			   JMenu menu = new JMenu("My Menu");
			   JMenuItem menuItem1 = new JMenuItem("option1");
			   JMenuItem  menuItem2 = new JMenuItem("option2");
			   JMenuItem menuItem3 = new JMenuItem("option3");
			   JMenuItem  menuItem4 = new JMenuItem("option4");
			   menuItem1.addActionListener(this);
			   
			   menu.add(menuItem1);
			   menuItem2.addActionListener(this);
			   menu.add(menuItem2);
			   menuItem3.addActionListener(this);
			   menu.add(menuItem3);
			   menuItem4.addActionListener(this);
			   menu.add(menuItem4);
			   menuBar.add(menu);

		   JFrame fr=new JFrame("With Menu"); 
		   fr.setJMenuBar(menuBar);
		   fr.add(a1,BorderLayout.CENTER);                            
		   
		   fr.setBounds(0,0,500,500);
		   fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		   fr.setVisible(true);

	}
  public void actionPerformed(ActionEvent ev){
 	 Object but=ev.getSource();
 	 if(but==jf){
 		JTextField bb=(JTextField)but;
 		  System.out.println(bb.getText());
 		//  a1=new EXE2();
 	 }
 	 else{
 	   if(but instanceof JMenuItem){
 		  JMenuItem mi=(JMenuItem) but;
 			String st=mi.getText();
 				jf.setText(st);
 				repaint();
 
 	   }
 	   else
 	   {
 	     JButton bb=(JButton)but;
 	     String text=bb.getActionCommand();	
 	     System.out.println(text);
 	   }
 	 }
  }
	/**
	 * @param args
	 */
	public static void main(String[] args) {
	
		WithMenu listener=new WithMenu();
		  

	}

	public void draw(Graphics g) {
		super.paintComponents(g);
		ImageIcon a=new ImageIcon("src/si.jpg");
		a.paintIcon(this, g, 150, 200);
	}
	
	}
	

