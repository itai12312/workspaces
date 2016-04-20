package project;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import project.RBTree.RBNode;

public class Gui {

   private JFrame mainFrame;
   //private JLabel headerLabel;
   private JLabel statusLabel;
   private JPanel controlPanel;
   //private JPanel tree;
    private RBTree x;
    private JTextArea nodetochange;
    private JTextArea asString;
    private JPanel[] panels;
    private JPanel[] panels2;

   public Gui(RBNode root,RBTree x){
      this.x=x;
       prepareGUI(root);
   }

   public static void main(String[] args){
       RBTree x=new RBTree();
       x.insert(50,"2");
       int[] arr = new int[]{8420,9933,3729,1920,7693,4711,7493,4895,7843,4584};
      for (int num : arr){
    	  x.insert(num, "");
      }
	  RBNode root = x.getRoot();
      Gui swingControlDemo = new Gui(root,x);/*root,x*/
      swingControlDemo.showEventDemo(root);
       System.out.println(x.order(root));
       //x.insert(10,"10");

   }
      
   private void prepareGUI(RBNode root){/*RBNode root*/
      mainFrame = new JFrame("Java SWING Examples");
      mainFrame.setSize(800,800);
      mainFrame.setLayout(new GridLayout(20, 1));
      
      //headerLabel = new JLabel("",JLabel.CENTER );
      statusLabel = new JLabel("",JLabel.CENTER);

      statusLabel.setSize(300,400);
      mainFrame.addWindowListener(new WindowAdapter() {
         public void windowClosing(WindowEvent windowEvent){
	        System.exit(0);
         }        
      });    
      controlPanel = new JPanel();
      controlPanel.setLayout(new FlowLayout());
      
      panels=new JPanel[15];
       RBNode [][] nodes=new RBNode[15][];
       int count =0 ;
       panels[0]=new JPanel();
       panels[0].add(nodetoJButton(root));
       nodes[0]=new RBNode[1];
       nodes[0][0]=root;
       mainFrame.add(panels[0]);
      for(int i=1;i<6;i++){
      panels[i] = new JPanel();
      panels[i].setLayout(new GridLayout(1, (int)Math.pow(2,i)));
          nodes[i]=new RBNode[(int)Math.pow(2,i)];
          for(int j=0;j<(int)Math.pow(2,i-1);j++){
                if(nodes[i-1][j]!=null) {
                    nodes[i][2 * j] = nodes[i - 1][j].getLeft();
                    nodes[i][2 * j + 1] = nodes[i - 1][j].getRight();
                }
              else{
                    nodes[i][2*j]=null;
                    nodes[i][2*j+1]=null;
                }
              if(nodes[i][2*j]!=null){
              panels[i].add(nodetoJButton(nodes[i][2*j]));
              }else{
                  panels[i].add(fromInt(count));
              }
              count++;
              if(nodes[i][2*j+1]!=null) {
                  panels[i].add(nodetoJButton(nodes[i][2 * j + 1]));
              }else{
                  panels[i].add(fromInt(count));
              }
              count++;
          }

          mainFrame.add(panels[i]);
      }

      
      panels2=new JPanel[15];
      RBNode [][] nodes2=new RBNode[15][];
      count =0 ;
      panels2[0]=new JPanel();
      panels2[0].add(nodetoJButton(root));
      nodes2[0]=new RBNode[1];
      nodes2[0][0]=root;
      mainFrame.add(panels2[0]);
     for(int i=1;i<6;i++){
     panels2[i] = new JPanel();
     panels2[i].setLayout(new GridLayout(1, (int)Math.pow(2,i)));
         nodes2[i]=new RBNode[(int)Math.pow(2,i)];
         for(int j=0;j<(int)Math.pow(2,i-1);j++){
               if(nodes2[i-1][j]!=null) {
                   nodes2[i][2 * j] = nodes2[i - 1][j].getLeft();
                   nodes2[i][2 * j + 1] = nodes2[i - 1][j].getRight();
               }
             else{
                   nodes2[i][2*j]=null;
                   nodes2[i][2*j+1]=null;
               }
             if(nodes2[i][2*j]!=null){
             panels2[i].add(nodetoJButton(nodes2[i][2*j]));
             }else{
                 panels2[i].add(fromInt(count));
             }
             count++;
             if(nodes2[i][2*j+1]!=null) {
                 panels2[i].add(nodetoJButton(nodes2[i][2 * j + 1]));
             }else{
                 panels2[i].add(fromInt(count));
             }
             count++;
         }

         mainFrame.add(panels2[i]);
     }
     
     
      //mainFrame.add(headerLabel);
      mainFrame.add(controlPanel);
      mainFrame.add(statusLabel);
      //mainFrame.add(tree);
      mainFrame.setVisible(true);
   }


    public void update() {

        for (int i = 0; i < 6; i++) {
            mainFrame.remove(panels[i]);

        }
            panels = new JPanel[15];
        RBNode[][] nodes = new RBNode[15][];
        int count = 0;
        panels[0] = new JPanel();
        panels[0].add(nodetoJButton(x.root));
        nodes[0] = new RBNode[1];
        nodes[0][0] = x.root;
        mainFrame.add(panels[0]);
        for (int i = 1; i < 6; i++) {
            panels[i] = new JPanel();
            panels[i].setLayout(new GridLayout(1, (int) Math.pow(2, i)));
            nodes[i] = new RBNode[(int) Math.pow(2, i)];
            for (int j = 0; j < (int) Math.pow(2, i - 1); j++) {
                if (nodes[i - 1][j] != null) {
                    nodes[i][2 * j] = nodes[i - 1][j].getLeft();
                    nodes[i][2 * j + 1] = nodes[i - 1][j].getRight();
                } else {
                    nodes[i][2 * j] = null;
                    nodes[i][2 * j + 1] = null;
                }
                if (nodes[i][2 * j] != null) {
                    panels[i].add(nodetoJButton(nodes[i][2 * j]));
                } else {
                    panels[i].add(fromInt(count));
                }
                count++;
                if (nodes[i][2 * j + 1] != null) {
                    panels[i].add(nodetoJButton(nodes[i][2 * j + 1]));
                } else {
                    panels[i].add(fromInt(count));
                }
                count++;
            }
            mainFrame.add(panels[i]);

        }

    }

    public void copy_before(){
        for (int i = 0; i < 6; i++) {
            mainFrame.remove(panels2[i]);

        }
            panels2 = new JPanel[15];
        RBNode[][] nodes2 = new RBNode[15][];
        int count = 0;
        panels2[0] = new JPanel();
        panels2[0].add(nodetoJButton(x.root));
        nodes2[0] = new RBNode[1];
        nodes2[0][0] = x.root;
        mainFrame.add(panels2[0]);
        for (int i = 1; i < 6; i++) {
            panels2[i] = new JPanel();
            panels2[i].setLayout(new GridLayout(1, (int) Math.pow(2, i)));
            nodes2[i] = new RBNode[(int) Math.pow(2, i)];
            for (int j = 0; j < (int) Math.pow(2, i - 1); j++) {
                if (nodes2[i - 1][j] != null) {
                    nodes2[i][2 * j] = nodes2[i - 1][j].getLeft();
                    nodes2[i][2 * j + 1] = nodes2[i - 1][j].getRight();
                } else {
                    nodes2[i][2 * j] = null;
                    nodes2[i][2 * j + 1] = null;
                }
                if (nodes2[i][2 * j] != null) {
                    panels2[i].add(nodetoJButton(nodes2[i][2 * j]));
                } else {
                    panels2[i].add(fromInt(count));
                }
                count++;
                if (nodes2[i][2 * j + 1] != null) {
                    panels2[i].add(nodetoJButton(nodes2[i][2 * j + 1]));
                } else {
                    panels2[i].add(fromInt(count));
                }
                count++;
            }
            mainFrame.add(panels2[i]);

        }

    }
    public JButton nodetoJButton(RBNode c){
        JButton a =new JButton(c.getKey()+"");
        a.setActionCommand(c.getKey()+" ");
        a.addActionListener(new ButtonClickListener());
        if(c.isRed()){
            a.setBackground(Color.RED);
        }else{
            a.setBackground(Color.BLACK);
        }
        return a;
    }

    public JButton fromInt(int i){
        JButton a =new JButton(i+"");
        a.setActionCommand(i+" ");
        a.addActionListener(new ButtonClickListener());
        return a;
    }

   private void showEventDemo(RBNode root){
      //headerLabel.setText("Control in action: Button");

      JButton okButton = new JButton("Insert");
      JButton submitButton = new JButton("Delete");
       JButton up = new JButton("update");
       JButton pr = new JButton("print");
       JButton lr = new JButton("LeftR");
       JButton rr = new JButton("RightR");

      okButton.setActionCommand("Insert");
      lr.setActionCommand("l");
      rr.setActionCommand("r");
       up.setActionCommand("Update");
      submitButton.setActionCommand("Delete");
       pr.setActionCommand("pr");

       lr.addActionListener(new ButtonClickListener());
       rr.addActionListener(new ButtonClickListener());
    pr.addActionListener(new ButtonClickListener());
       okButton.addActionListener(new ButtonClickListener());
       up.addActionListener(new ButtonClickListener());
      submitButton.addActionListener(new ButtonClickListener()); 
       nodetochange = new JTextArea("Num");
       nodetochange.setName("text");
       nodetochange.setEditable(true);
       

        controlPanel.add(nodetochange);
       controlPanel.add(pr);
       controlPanel.add(lr);
       controlPanel.add(rr);
       controlPanel.add(up);
      controlPanel.add(okButton);
      controlPanel.add(submitButton);
      /*asString = new JTextArea("Enter string");
      asString.setName("asString");
      asString.setEditable(true);
      controlPanel.add(asString);*/
      
      /*JButton[] tree1=new JButton[root.size(root)];
      fill_array(tree1,root,0);
      for(int k=0;k<tree1.length&&k<10;k++){
      tree.add(tree1[k]);
      }*/

      mainFrame.setVisible(true);
      
   }
   
   public void fill_array(JButton[] array, RBNode r, int i){
	   if(-1<i && i<array.length){
	   array[i]=new JButton(r.getKey()+"");
	   if(r.isRed()){
	   array[i].setBackground(Color.RED);
	   }else{
		   array[i].setBackground(Color.BLUE);  
	   }
	   if(r.getLeft()!=null){
		   fill_array(array,r.getLeft(),2*i);
	   }
	   if(r.getRight()!=null){
		   fill_array(array,r.getRight(),2*i+1);
	   }
	   }
   }

   private class ButtonClickListener implements ActionListener{
      public void actionPerformed(ActionEvent e) {
         String command = e.getActionCommand();
          statusLabel.setText(command+" clicked");
          if(command.equals("Insert")){
        	  copy_before();
              x.insert(Integer.parseInt(nodetochange.getText()),"a");
              update();
          }else if(command.equals("Delete")){
        	  copy_before();
              x.delete(Integer.parseInt(nodetochange.getText()));
              update();
          }else if(command.equals("Update")){
              update();
          }else if (command.equals("pr")){
              System.out.println(x.order(x.root));
          }else if (command.equals("l")){
              x.leftRotate(x.search_node(Integer.parseInt(nodetochange.getText())));
              update();
          }else if (command.equals("r")){
        	  x.leftRotate(x.search_node(Integer.parseInt(nodetochange.getText())));
        	  update();
          }
         //if( command.equals( "OK" ))  {
         //   statusLabel.setText("Ok Button clicked.");
         //}
         //else if( command.equals( "Submit" ) )  {
         //   statusLabel.setText("Submit Button clicked.");
         //}
         //else  {
          //  statusLabel.setText("Cancel Button clicked.");
         //}
      }		
   }
}