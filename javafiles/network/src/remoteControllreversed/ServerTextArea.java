package remoteControllreversed;

/*
 * TextSamplerDemo.java is a 1.4 application that requires the
 * following files:
 *   TextSamplerDemoHelp.html (which references images/dukeWaveRed.gif)
 *   images/Pig.gif
 *   images/sound.gif
 */
import javax.swing.*;

import java.awt.*;             
import java.awt.event.*;       
import java.sql.Timestamp;
//add timestamp to messages+sender/from whom/reciever
public class ServerTextArea extends JPanel{
	
	private String newline = "\n";   
	protected JScrollPane areaScrollPane=null;
	protected JTextArea displayInfo = new JTextArea();
	private JScrollPane paneScrollPane=null;
	JFrame frame = new JFrame("TextSamplerDemo");

	private JTextArea toSend = new JTextArea();

	private Client multi=null;

	JTextField ip=new JTextField("ws1");
	JTextField port=new JTextField("4000",7);
	public ServerTextArea(Client multi) {
		super();
		this.multi=multi;
		setLayout(new BorderLayout());
		//Create a text area.

		//Create a text pane.
		displayInfo.setFont(new Font("Serif", Font.ITALIC, 16));
		displayInfo.setLineWrap(true);
		displayInfo.setWrapStyleWord(true);
		areaScrollPane = new JScrollPane(displayInfo);
		areaScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		areaScrollPane.setPreferredSize(new Dimension(150, 150));

		add(areaScrollPane,BorderLayout.NORTH);        


		toSend.setFont(new Font("Serif", Font.ITALIC, 16));
		toSend.setLineWrap(true);
		toSend.setWrapStyleWord(true);
		paneScrollPane = new JScrollPane(toSend);
		paneScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
		paneScrollPane.setPreferredSize(new Dimension(150, 155));
		paneScrollPane.setMinimumSize(new Dimension(10, 10));


		add(paneScrollPane,BorderLayout.SOUTH);  

		JButton send=new JButton("send");
		send.setSize(60, 40);
		send.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				send(toSend.getText(),Integer.parseInt(port.getText()),ip.getText().trim());
			}});
		add(send,BorderLayout.CENTER);
		JButton con=new JButton("con");
		con.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent arg0) {
				connect(Integer.parseInt(port.getText()),ip.getText());
			}});
		add(con,BorderLayout.CENTER);
		add(ip,BorderLayout.WEST);
		add(port,BorderLayout.EAST);
		create(multi);
	}    

	protected void connect(int port, String host) {
		multi.connect(port,host);
	}

	protected void send(String text,int port,String host) {
		displayInfo.append("send at time:"+new Timestamp(new java.util.Date().getTime())+"\n");
		displayInfo.append(text+" "+port+" "+ host);
		multi.sentToSpecificSocker(text, port, host);
		toSend.setText("");
	}

	public String getToSendText(){
		return toSend.getText();
	}

	public String viewMessages(){
		return displayInfo.getText();
	}

	public void create(Client mu) {
		JFrame.setDefaultLookAndFeelDecorated(true);

		//Create and set up the window.

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


		//Create and set up the content pane.
		///    JComponent newContentPane = this;
		///   newContentPane.setOpaque(true); //content panes must be opaque
		///  frame.setContentPane(newContentPane);
		frame.add(this,BorderLayout.NORTH);
		//frame.add(new p(),BorderLayout.SOUTH);
		//Display the window.
		frame.pack();
		frame.setVisible(true);
		frame.setTitle("Server");
		//p a=new p();
		
		
		 JFrame fr=new JFrame("action");
		    fr.add(new p());
			fr.setSize(500,500);
			fr.setBackground(Color.green);
			fr.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		    fr.setVisible(true);
	}

	public void write(String s){
		displayInfo.append(s);
	}


	public static void main(String[]args) {
		/* JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
        MultiThreadServer mu=null;
        JFrame frame=new JFrame();
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);


        //Create and set up the content pane.
        JComponent newContentPane = new ServerTextArea(mu);
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);*/
		Client s=null;
		ServerTextArea a=new ServerTextArea(s);
	}
	private class p extends JPanel implements KeyListener,MouseListener,MouseMotionListener,MouseWheelListener{
		public void keyPressed(KeyEvent arg0) {
			frame.setTitle("keyPressed");
			multi.sendToAll(arg0.getKeyCode()+",press");
		}

		public void keyReleased(KeyEvent arg0) {
			frame.setTitle("keyReleased");
			multi.sendToAll(arg0.getKeyCode()+",reles");
		}

		public void keyTyped(KeyEvent arg0) {
			frame.setTitle("keyTyped");
			multi.sendToAll(arg0.getKeyCode()+",typed");
		}

		public void mouseClicked(MouseEvent arg0) {
			frame.setTitle("mouseclicked");
			multi.sendToAll(arg0.getXOnScreen()+","+arg0.getYOnScreen()+",clicked"+butnum(arg0.getButton()));
		}

		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}

		public void mousePressed(MouseEvent arg0) {
			frame.setTitle("mousePressed");
			multi.sendToAll(arg0.getXOnScreen()+","+arg0.getYOnScreen()+",pressed"+butnum(arg0.getButton()));
			requestFocus();
		}

		public void mouseReleased(MouseEvent arg0) {
			frame.setTitle("mouseReleased");
			multi.sendToAll(arg0.getXOnScreen()+","+arg0.getYOnScreen()+",relesed"+butnum(arg0.getButton()));
		}
		
		public int butnum(int modifier/*,int but1,int but2,int but3*/){
			if((modifier & InputEvent.BUTTON3_MASK)== InputEvent.BUTTON3_MASK){
				return InputEvent.BUTTON3_DOWN_MASK;
			}else if((modifier & InputEvent.BUTTON2_MASK)== InputEvent.BUTTON2_MASK){
				return InputEvent.BUTTON2_DOWN_MASK;
			}else if((modifier & InputEvent.BUTTON1_MASK)== InputEvent.BUTTON1_MASK){
				return InputEvent.BUTTON1_DOWN_MASK;
			}
			return modifier;
		}
		public p() {
			super();
			setLayout(new BorderLayout());
			addMouseListener(this);
			addMouseMotionListener(this);
			addMouseWheelListener(this);
			addKeyListener(this);
			//setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);
			//  setTitle("controll");
		}
		public void mouseWheelMoved(MouseWheelEvent e) {
			frame.setTitle("mouseWheelMoved");
			multi.sendToAll(e.getClickCount()+","+e.getButton()+",whemmed"+butnum(e.getButton()));
		}
		public void mouseDragged(MouseEvent arg0) {
			frame.setTitle("mouseDragged");
			multi.sendToAll(arg0.getXOnScreen()+","+arg0.getYOnScreen()+",dragged"+butnum(arg0.getButton()));
		}
		public void mouseMoved(MouseEvent arg0) {
			frame.setTitle("mouseMoved");
			multi.sendToAll(arg0.getXOnScreen()+","+arg0.getYOnScreen()+",moveded"+butnum(arg0.getButton()));
		}
	}
}
