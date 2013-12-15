package paint;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class EXE1 extends JFrame implements ActionListener{
	JLabel picture=new JLabel(new ImageIcon("1.gif"));
	public EXE1(){
	
	ButtonGroup group=new ButtonGroup();
	  JPanel a1=new JPanel(new FlowLayout());
	  JFrame fr=new JFrame("Mixed Container"); 
		
		String []p =new String[4];
		p[0]="1";p[1]="2";p[2]="3";p[3]="4";
		JComboBox petList = new JComboBox(p);
		JTextField a11=new JTextField("enter file name here");
JMenuBar menuBar = new JMenuBar();
JMenu menu = new JMenu("A Menu");
a1.setPreferredSize(new Dimension(100,50));
	   for(int i=0;i<5;i++){
		      JButton b=new JButton(new ImageIcon(i+".gif"));
		      b.addActionListener(this);
		      b.setActionCommand(i+"ddfd");
		      a1.add(b);
			JRadioButton b1=new JRadioButton(i+"");
		      b1.addActionListener(this);
		      b1.setActionCommand(i+"");
		if(i==0)
				b1.setSelected(true);		     
		group.add(b1);
		a1.add(b1);
JMenuItem c=new JMenuItem(i+"");
		      c.addActionListener(this);
		      c.setActionCommand(i+"");
menu.add(c);
		   }
		   
petList.addActionListener(this);
a11.addActionListener(this);
a1.add(petList);
a1.add(a11);
a1.add(picture);
//a1.add(group);
menuBar.add(menu);
fr.add(a1);
fr.setJMenuBar(menuBar);
fr.setBounds(0,0,500,500);
fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
fr.setVisible(true);
	
	
	
}
	public static void main(String[]args){
	  EXE1 a=new EXE1();
	
}


public void actionPerformed(ActionEvent e) {
	Object help=e.getSource();
	if(help instanceof JButton){
		System.out.print(((JButton) help).getActionCommand().charAt(0)+".gif");
	picture.setIcon(new ImageIcon (((JButton) help).getActionCommand().charAt(0)+".gif"));}
	if(help instanceof JComboBox)
		picture.setIcon(new ImageIcon(((String) ((JComboBox) help).getSelectedItem()).charAt(0)+".gif"));
	if(help instanceof JTextField)
		picture.setIcon(new ImageIcon(((JTextField) help).getText().charAt(0)+".gif"));
	if(help instanceof JRadioButton)
		picture.setIcon(new ImageIcon(((JRadioButton) help).getActionCommand().charAt(0)+".gif"));
	if(help instanceof JMenuItem)
		picture.setIcon(new ImageIcon(((JMenuItem) help).getText().charAt(0)+".gif"));

}

}
