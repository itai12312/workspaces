package XO;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.*;
import javax.swing.*;
public class XO extends JFrame implements ActionListener{
	Color c=Color.RED;
	JPanel but;
	//String message;int x=400,y=400;
	JTextArea a=new JTextArea();
	int size=3;
	JButton[][]b=new JButton[size][size];
	public static void main(String[] args) {
		XO xo=new XO();
	}
	public XO(){
		setLayout(new GridLayout(2,1));
		but=new JPanel();
		but.setLayout(new GridLayout(3,3));
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				b[i][j]=new JButton();
				JButton r=b[i][j];
				r.setActionCommand(i+""+j);
				r.addActionListener(this);
				r.setBackground(Color.WHITE);
				but.add(r);
			}
		}
		add(but);
		a.setEditable(false);
		add(a);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setSize(600,600);
		setVisible(true);
	}
	public void actionPerformed(ActionEvent arg0) {
		//if(arg0.getSource() instanceof JButton){
			JButton r=(JButton)arg0.getSource();
			if(r.getBackground()==Color.WHITE){
				r.setBackground(c);
				r.setEnabled(false);
				c=(c==Color.BLUE)?Color.RED:Color.BLUE;
			}
		//}
		if(ifWon()!=0){
			a.append("player number "+ifWon()+" has won"+"\n");
			clear();
		}else if(ifTie()){
			clear();
		}
	}
	private void clear() {
		try {
			Thread.sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				b[i][j].setBackground(Color.WHITE);
				b[i][j].setEnabled(true);
			}
		}
		c=Color.RED;
	}
	public int ifWon(){
		for(int i=0;i<size;i++){
			if(b[i][0].getBackground()==b[i][1].getBackground()&&b[i][1].getBackground()==b[i][2].getBackground()&&b[i][2].getBackground()!=Color.WHITE){
				return b[i][0].getBackground()==Color.RED?1:0;
			}
			if(b[0][i].getBackground()==b[1][i].getBackground()&&b[1][i].getBackground()==b[2][i].getBackground()&&b[2][i].getBackground()!=Color.WHITE){
				return b[0][i].getBackground()==Color.RED?1:0;
			}
		}
		if(b[0][0].getBackground()==b[1][1].getBackground()&&b[1][1].getBackground()==b[2][2].getBackground()&&b[2][2].getBackground()!=Color.WHITE){
			return b[0][0].getBackground()==Color.RED?1:0;
		}
		if(b[2][0].getBackground()==b[1][1].getBackground()&&b[1][1].getBackground()==b[0][2].getBackground()&&b[0][2].getBackground()!=Color.WHITE){
			return b[2][0].getBackground()==Color.RED?1:0;
		}

		return 0;		
	}
	public boolean ifTie(){
		for(int i=0;i<size;i++){
			for(int j=0;j<size;j++){
				if(b[i][j].getBackground()==Color.WHITE) 
					return false;
			}
		}
		a.append("tie"+"\n");
		return true;
	}
}