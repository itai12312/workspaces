package Paint;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class EXE5 extends JFrame implements ActionListener{
	JTextField b=new JTextField();
	
	public EXE5(){
	JPanel a=new JPanel();
	String []o={"1","2","3"};
	b.setText("d");
	JComboBox petList = new JComboBox(o);	
	petList.addActionListener(this);
	a.add(b);
	a.add(petList);
	add(a);
	
}
public static void main(String[]args){
EXE5 a=new EXE5();
a.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
a.setSize(500,500);
a.setVisible(true);
	
}

public void actionPerformed(ActionEvent e) {
	System.out.println((String)((JComboBox) e.getSource()).getSelectedItem());
	b.setText((String)((JComboBox) e.getSource()).getSelectedItem());

}
}
