package mouse;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
 
public class WindowEventDemo extends JFrame  implements WindowListener  {
   
    private JTextArea display;
   
     
    public static void main(String[] args) {
    	WindowEventDemo wd=new WindowEventDemo();
    }

     
    public WindowEventDemo() {
        super("Window Events Demo");
      //Create and set up the window.
      //this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
         
        //Set up the content pane.
        display = new JTextArea();
        display.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(display);
        scrollPane.setPreferredSize(new Dimension(500, 450));
        add(scrollPane);
         
        addWindowListener(this);  
        //Display the window.
        this.pack();
        this.setVisible(true);
    }

    //Removing the window from the screen.
    public void windowClosing(WindowEvent e) {
       System.out.println("The Window closing");
    }
     
    public void windowClosed(WindowEvent e) {
  
    	System.out.println("WindowListener method called: windowClosed.\n");
    }
    
    //Showing a window for the first time. 
    public void windowOpened(WindowEvent e) {
    	display.append("WindowListener method called: windowOpened.\n");
    }
    // Reducing the window to an icon on the desktop 
    public void windowIconified(WindowEvent e) {
    	display.append("WindowListener method called: windowIconified.\n");
    }
    // Restoring the window to its original size. 
    public void windowDeiconified(WindowEvent e) {
    	display.append("WindowListener method called: windowDeiconified.\n");
    }
    // This window is either the focused window, or owns the focused window. 
    public void windowActivated(WindowEvent e) {
    	display.append("WindowListener method called: windowActivated.\n");
    }
   // This window has lost the focus.  
    public void windowDeactivated(WindowEvent e) {
    	display.append("WindowListener method called: windowDeactivated.\n");
    }

}