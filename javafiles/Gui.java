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
    private final int levels=8;

   public Gui(RBNode root,RBTree x){
      this.x=x;
       prepareGUI(root);
   }

   public static void main(String[] args){
       RBTree x=new RBTree();
       x.insert(50,"2");
        for(int i=0;i<15;i++){
            x.insert((int)(10000*Math.random())+1," s");
        }
	  RBNode root = x.getRoot();
      Gui swingControlDemo = new Gui(root,x);
      swingControlDemo.showEventDemo(root);

   }
      
   private void prepareGUI(RBNode root){
      mainFrame = new JFrame("Java SWING Examples");
      mainFrame.setSize(800,800);
      mainFrame.setLayout(new GridLayout(20, 1));
      
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
      for(int i=1;i<levels;i++){
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
     for(int i=1;i<levels;i++){
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

        for (int i = 0; i < levels; i++) {
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
        for (int i = 1; i < levels; i++) {
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
        for (int i = 0; i < levels; i++) {
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
        for (int i = 1; i < levels; i++) {
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
        a.setActionCommand(c.getKey()+"");
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
        String[]func={"insert","delete","update","print","LeftR","RightR","rank","succesor","size"};
       for(int i=0;i<func.length;i++){
           JButton f=new JButton(func[i]);
           f.setActionCommand(func[i]);
           f.addActionListener(new ButtonClickListener());
           controlPanel.add(f);
       }
       nodetochange = new JTextArea("Num");
       nodetochange.setName("text");
       nodetochange.setEditable(true);
       if(x.root!=null) {
           nodetochange.setText(x.root.getKey()+"");
       }
       controlPanel.add(nodetochange);

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
          String c=command.trim();
          statusLabel.setText(command+" clicked");
          if(command.equals("insert")){
        	  copy_before();
              x.insert(Integer.parseInt(nodetochange.getText()),"a");
              update();
          }else if(command.equals("delete")){
        	  copy_before();
              x.delete(Integer.parseInt(nodetochange.getText()));
              update();
          }else if(command.equals("Update")){
              update();
          }else if (command.equals("print")){
              System.out.println(x.order(x.root));
          }else if (command.equals("LeftR")){
              x.leftRotate(x.search_node(Integer.parseInt(nodetochange.getText())));
              update();
          }else if (command.equals("RightR")){
        	  x.rightRotate(x.search_node(Integer.parseInt(nodetochange.getText())));
        	  update();
          }else if(command.equals("succesor")){
              RBNode t=x.search_node(Integer.parseInt(nodetochange.getText()));
              statusLabel.setText("succesor of "+t.getKey()+" is " +x.succesor(t));
          }else if(command.equals("size")){
              RBNode t=x.search_node(Integer.parseInt(nodetochange.getText()));
              statusLabel.setText("size of "+t.getKey()+" is " +x.size(t));
          }else if(command.equals("rank")){
              RBNode t=x.search_node(Integer.parseInt(nodetochange.getText()));
              statusLabel.setText("rank of "+t.getKey()+" is " +x.rank(t));
          }else if(0<Integer.parseInt(c)){
              RBNode p=x.search_node(Integer.parseInt(c));
              nodetochange.setText(c);
              if(p==null){
                  statusLabel.setText(c+" clicked");
              }else {
                  statusLabel.setText(p.toString());
              }
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