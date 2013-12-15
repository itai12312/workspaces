package Paint;

import javax.swing.JFrame;
import javax.swing.JButton;
import java.awt.BorderLayout;
import javax.swing.JPanel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.JComboBox;
import javax.swing.JMenuBar;
import javax.swing.JTextField;
import javax.swing.JRadioButton;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class mesi extends JFrame{
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
public static void main(String[]args){mesi a=new mesi();}

public mesi(){
	setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	setVisible(true);
	setSize(500, 500);
	
	JPanel panel = new JPanel();
	getContentPane().add(panel, BorderLayout.NORTH);
	ButtonGroup a=new ButtonGroup();
	ButtonGroup b=new ButtonGroup();
	JButton btnChagnepicture = new JButton("chagnepicture");
	btnChagnepicture.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
		}
	});
	
	JComboBox comboBox = new JComboBox();
	comboBox.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
		}
	});
	
	textField = new JTextField();
	textField.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
		}
	});
	
	JRadioButton rdbtnNewRadioButton_1 = new JRadioButton("New radio button");
	a.add(rdbtnNewRadioButton_1);
	panel.add(rdbtnNewRadioButton_1);
	
	JRadioButton rdbtnNewRadioButton = new JRadioButton("New radio button");
	a.add(rdbtnNewRadioButton);
	rdbtnNewRadioButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
		}
	});
	panel.add(rdbtnNewRadioButton);
	panel.add(textField);
	textField.setColumns(10);
	panel.add(comboBox);
	panel.add(btnChagnepicture);
	
	JPanel panel_1 = new JPanel();
	getContentPane().add(panel_1, BorderLayout.SOUTH);
	
	JButton btnNewButton = new JButton("New button");
	btnNewButton.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
		}
	});
	panel_1.add(btnNewButton);
	
	textField_3 = new JTextField();
	panel_1.add(textField_3);
	textField_3.setColumns(10);
	
	textField_2 = new JTextField();
	panel_1.add(textField_2);
	textField_2.setColumns(10);
	
	textField_1 = new JTextField();
	panel_1.add(textField_1);
	textField_1.setColumns(10);
	
	JPanel panel_2 = new JPanel();
	getContentPane().add(panel_2, BorderLayout.CENTER);
	
	JComboBox comboBox_1 = new JComboBox();
	comboBox_1.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
		}
	});
	
	JPanel panel_4 = new JPanel();
	panel_2.add(panel_4);
	
	JComboBox comboBox_2 = new JComboBox();
	comboBox_2.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
		}
	});
	
	textField_4 = new JTextField();
	panel_4.add(textField_4);
	textField_4.setColumns(10);
	panel_4.add(comboBox_2);
	panel_2.add(comboBox_1);
	
	JMenuBar menuBar = new JMenuBar();
	setJMenuBar(menuBar);
	
	JMenu mnNewMenu = new JMenu("New menu");
	menuBar.add(mnNewMenu);
	
	JMenuItem mntmNewMenuItem = new JMenuItem("New menu item");
	mnNewMenu.add(mntmNewMenuItem);
	
	JMenuItem mntmNewMenuItem_1 = new JMenuItem("New menu item");
	mnNewMenu.add(mntmNewMenuItem_1);
	
	JMenuItem mntmNewMenuItem_2 = new JMenuItem("New menu item");
	mnNewMenu.add(mntmNewMenuItem_2);
	
	JMenuItem mntmNewMenuItem_3 = new JMenuItem("New menu item");
	mnNewMenu.add(mntmNewMenuItem_3);
	
	JPanel panel_3 = new JPanel();
	menuBar.add(panel_3);
	
	JRadioButton radioButton_1 = new JRadioButton("");
	b.add(radioButton_1);
	panel_3.add(radioButton_1);
	
	JRadioButton rdbtnNewRadioButton_2 = new JRadioButton("New radio button");
	b.add(rdbtnNewRadioButton_2);
	panel_3.add(rdbtnNewRadioButton_2);
	
	JRadioButton radioButton = new JRadioButton("");
	b.add(radioButton);
	panel_3.add(radioButton);
	
	JButton button = new JButton("");
	button.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
		}
	});
	panel_3.add(button);
}
}
