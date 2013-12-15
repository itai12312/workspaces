package AlbomAvodot;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class PictLuah extends JPanel implements ActionListener{  
        
         private JTextField tf; 
         private JComboBox cb;
         String pict;
        
        public PictLuah(){
        	
          setBorder(BorderFactory.createEmptyBorder(5,5,5,5));
          
          tf=new JTextField(20);
          String[] cbStrings = { "Rabbit", "Monkey", "Bear"};
          cb = new JComboBox(cbStrings);
          cb.setSelectedIndex(0);
          pict="2.gif";
          add(tf);  
          add(cb);
          cb.addActionListener(this);
         
        }
       public void paintComponent(Graphics g)
		{
			super.paintComponent(g);
			ImageIcon im=new ImageIcon(pict);
			im.paintIcon(this,g,10,50);
			
		}
 
       public void actionPerformed(ActionEvent ev){  
      
  	    
  	   if(  ev.getSource()==cb) 
  	   { 
  	       tf.setText((String) cb.getSelectedItem());  
  	       System.out.println(cb.getSelectedIndex());
  	       if( cb.getSelectedIndex()==0)
  	          pict="2.gif";
                 
  	       if( cb.getSelectedIndex()==1)
  	          pict="3.gif";
  	       
  	      if( cb.getSelectedIndex()==2)
  	          pict="4.gif";
  	      repaint();    
  	   }  
  }

  public static void main(String[] args)

  {

    PictLuah app=new PictLuah();
    JFrame frame=new JFrame();
    frame.getContentPane().add(app);
    frame.setSize(500,500);
    frame.setVisible(true);
      
      }
      
      }