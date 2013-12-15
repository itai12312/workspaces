package GraphicsChat;

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
    
    MultiThreadServer multi=null;
    
    JTextField ip=new JTextField("ip");
    JTextField port=new JTextField("port",7);
    public ServerTextArea(MultiThreadServer multi) {
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
        areaScrollPane.setPreferredSize(new Dimension(250, 250));
       
        add(areaScrollPane,BorderLayout.NORTH);        
        
        
        toSend.setFont(new Font("Serif", Font.ITALIC, 16));
        toSend.setLineWrap(true);
        toSend.setWrapStyleWord(true);
        paneScrollPane = new JScrollPane(toSend);
        paneScrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        paneScrollPane.setPreferredSize(new Dimension(250, 155));
        paneScrollPane.setMinimumSize(new Dimension(10, 10));
        
       
        add(paneScrollPane,BorderLayout.SOUTH);  
        
       JButton send=new JButton("send");
       send.addActionListener(new ActionListener(){
    	   public void actionPerformed(ActionEvent arg0) {
			send(toSend.getText(),Integer.parseInt(port.getText()),ip.getText());
		}});
       add(send);
       add(ip,BorderLayout.WEST);
       add(port,BorderLayout.EAST);
       create(multi);
       }    

    protected void send(String text,int port,String host) {
		displayInfo.append("send at time:"+new Timestamp(new java.util.Date().getTime()));
    	multi.sentToSpecificSocker(text, port, host);
    	toSend.setText("");
	}

    public String getToSendText(){
    	return toSend.getText();
    }
    
    public String viewMessages(){
    	return displayInfo.getText();
    }
    
	public void create(MultiThreadServer mu) {
        JFrame.setDefaultLookAndFeelDecorated(true);

        //Create and set up the window.
       
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
      
        //Create and set up the content pane.
        JComponent newContentPane = this;
        newContentPane.setOpaque(true); //content panes must be opaque
        frame.setContentPane(newContentPane);

        //Display the window.
        frame.pack();
        frame.setVisible(true);
        frame.setTitle("Server");
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
		MultiThreadServer s=null;
		ServerTextArea a=new ServerTextArea(s);
    }

}
