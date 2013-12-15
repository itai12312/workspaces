package paint;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

public class EXE2 extends JFrame implements ActionListener{
	String[] colors={"red","blue","green"};
	 Color[] color={Color.RED,Color.BLUE,Color.GREEN};
	 JPanel a1=new JPanel();
public EXE2(){

	JMenuBar menuBar = new JMenuBar();
	JMenu menu1 = new JMenu("color menu");
	for(int i=0;i<colors.length;i++){
	  JMenuItem b=new JMenuItem(i+colors[i]);
			      b.addActionListener(this);
			      b.setActionCommand(i+"");
	menu1.add(b);
	}
	menuBar.add(menu1);
	setJMenuBar(menuBar);
	add(a1);
	
}
public static void main(String[]args){
	EXE2 a=new EXE2();
	a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	a.setSize(500,500);
	a.setVisible(true);
}

public void actionPerformed(ActionEvent e) {
	Object help=e.getSource();
	char a=((JMenuItem) help).getText().charAt(0);
	if(a=='0')
	a1.setBackground(color[0]);
	if(a=='1')
		a1.setBackground(color[1]);
	if(a=='2')
		a1.setBackground(color[2]);
	
}
}
