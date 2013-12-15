package Paint;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EXE3 extends JFrame implements ActionListener{
	JTextField a=new JTextField();
	JTextField b=new JTextField();
	JTextField c=new JTextField();
	 JTextField[] jbs={a,b,c};
	 int count=0;
public EXE3(){
	
	 JPanel a2=new JPanel(new FlowLayout());
	 jbs[0].setText("OH");
	 jbs[1].setText("O");
	 jbs[2].setText("O");
	 JButton ab=new JButton("mesima 3");
	 ab.addActionListener(this);
	 ab.setActionCommand("m3");
	 for(int i=0;i<jbs.length;i++){
	 a2.add(jbs[i]);}
	 a2.add(ab);
	 add(a2);
	
}
public static void main(String[]args){
	EXE3 a=new EXE3();
	a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	a.setSize(500,500);
	a.setVisible(true);
}

public void actionPerformed(ActionEvent arg0) {
		jbs[count].setText("O");
	if (count <2){
		count++;
	}
	else count=0;
	jbs[count].setText("OH");
	System.out.println(count);
}
}
