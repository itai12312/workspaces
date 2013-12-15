package Paint;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JRadioButton;

public class EXE4 extends JFrame implements ActionListener{
	 JRadioButton[] jbs1={new JRadioButton(),new JRadioButton(),new JRadioButton()};
		int count=0;
public EXE4(){
	JPanel a2=new JPanel();

	jbs1[0].setSelected(true);
	JButton ab1=new JButton("mesima 4");
	ab1.addActionListener(this);
	ab1.setActionCommand("m3");
	a2.add(ab1);
	for(int i=0;i<jbs1.length;i++){
	a2.add(jbs1[i]);}
add(a2);
}

public static void main(String[]args){
	EXE4 a=new EXE4();
	a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	a.setSize(500,500);
	a.setVisible(true);
	
}

public void actionPerformed(ActionEvent e) {
	count=(count++)%3;
	jbs1[count].setSelected(true);
	
}
}
