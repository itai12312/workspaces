package paint;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import javax.swing.*;

import java.awt.event.*;
public class Mesimot extends JFrame implements ActionListener{
	 static JLabel picture=new JLabel(new ImageIcon("home.jpg"));
	 int count=0;
	 JFrame fr=new JFrame("Mixed Container"); 
	 String[] colors={"red","blue","green"};
	 Color[] color={Color.RED,Color.BLUE,Color.GREEN};
	 JTextField m5=new JTextField("m5");
	 JPanel a1=new JPanel(new FlowLayout());
	 JTextField[] jbs={new JTextField(),new JTextField(),new JTextField()};
	 JRadioButton[] jbs1={new JRadioButton(),new JRadioButton(),new JRadioButton()};
public Mesimot(){
//mesima 1	
		  ButtonGroup group=new ButtonGroup();
		
		JLabel picture=new JLabel(new ImageIcon("1.jpg"));
		String []p =new String[4];
		p[0]="1";p[1]="2";p[2]="3";p[3]="4";
		JComboBox petList = new JComboBox(p);
		JTextField a11=new JTextField("enter file name here");
JMenuBar menuBar = new JMenuBar();
JMenu menu = new JMenu("A Menu");


		a1.setPreferredSize(new Dimension(100,50));
		   
		   
		   for(int i=0;i<5;i++){
		      JButton b=new JButton(new ImageIcon(i+".jpg"));
		      b.addActionListener(this);
		      b.setActionCommand("bu"+i);
		      a1.add(b);
			JRadioButton b1=new JRadioButton(i+"");
		      b1.addActionListener(this);
		      b1.setActionCommand("rb"+i);
		if(i==0)
				b1.setSelected(true);		     
 		group.add(b1);
  JMenuItem c=new JMenuItem(i+"");
		      c.addActionListener(this);
		      c.setActionCommand("mi"+i);
menu.add(c);
		   }
		   
petList.addActionListener(this);
a11.addActionListener(this);
a1.add(petList);
a1.add(a11);
a1.add(picture);
//a1.add(group);
menuBar.add(menu);
fr.setJMenuBar(menuBar);

//mesima 2

JMenu menu1 = new JMenu("color menu");
for(int i=0;i<colors.length;i++){
  JMenuItem b=new JMenuItem(colors[i]);
		      b.addActionListener(this);
		      b.setActionCommand("mq"+i);
menu1.add(b);
}
menuBar.add(menu1);
//mesima 5
JMenu menu2 = new JMenu("mesima5");
for(int i=0;i<colors.length;i++){
  JMenuItem b=new JMenuItem("m5"+i);
		      b.addActionListener(this);
		      b.setActionCommand("m5"+i);
menu2.add(b);
}

a1.add(m5);
menuBar.add(menu2);

//mesima 3
		   JPanel a2=new JPanel(new FlowLayout());


jbs[0].setText("OH");
JButton ab=new JButton("mesima 3");
ab.addActionListener(this);
ab.setActionCommand("m3");
for(int i=0;i<jbs.length;i++){
a2.add(jbs[i]);}
a2.add(ab);

//mesima 4

int count1=0;
jbs1[0].setSelected(true);
JButton ab1=new JButton("mesima 4");
ab1.addActionListener(this);
ab1.setActionCommand("m3");
a2.add(ab1);
for(int i=0;i<jbs.length;i++){
a2.add(jbs[i]);}

		   JPanel a3=new JPanel();
		   a3.setBackground(Color.green);
		   a3.setPreferredSize(new Dimension(200,200));
				  
		   JTextField jf=new JTextField(20);
		   a2.add(jf);
		   
		   //a3.add(picture);
		 
		
		   
		   fr.add(a1,BorderLayout.CENTER);                            
		   fr.add(a2,BorderLayout.WEST);
		   fr.add(a3,BorderLayout.NORTH);
		   fr.setBounds(0,0,500,500);
		   fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		   fr.setVisible(true);


	}

	@Override
	public void actionPerformed(ActionEvent e) {

Object help=e.getSource();
		if(help instanceof JButton){
		if((boolean)((JButton) help).getActionCommand().substring(0,1).equals("br"))
		picture.setIcon(new ImageIcon (((JButton) help).getActionCommand().indexOf(2)+".jpg"));
		else if(((JButton) help).getActionCommand().equals("m3")){
jbs[count].setText("");
		count=(count++)%3;
		jbs[count].setText("OH");}
else{
		count=(count++)%3;
		jbs1[count].setSelected(true);
}
		}
		if(help instanceof JComboBox){
		if(((String) ((JComboBox)help).getSelectedItem()).indexOf(2)=='i')
		picture.setIcon(new ImageIcon(((String) ((JComboBox) help).getSelectedItem()).indexOf(2)+".jpg"));
else if(((String) ((JComboBox) help).getSelectedItem()).substring(0,1).equals("m5"))
m5.setText((String)((JComboBox) e.getSource()).getSelectedItem());
else
		a1.setBackground(color[( (String) ((JComboBox) help).getSelectedItem()).indexOf(2)]);	
}
		if(help instanceof JTextField){
		picture.setIcon(new ImageIcon(((JTextField) help).getText().indexOf(2)+".jpg"));}
if(help instanceof JRadioButton){
		picture.setIcon(new ImageIcon(((JRadioButton) help).getActionCommand().indexOf(2)+".jpg"));}
if(help instanceof JMenuItem){
		picture.setIcon(new ImageIcon(((JMenuItem) help).getText().indexOf(2)+".jpg"));}
	



	
	}
	public static void main(String[] args) {
		Mesimot mc=new Mesimot();
	}
}
