package remotecontrol2;
1  import java.awt.*;
2  import java.awt.event.*;
3  import javax.swing.*;
4  import javax.swing.border.LineBorder;
5  import java.io.*;
6  import java.net.*;
7 
8  public class TicTacToeClient extends JApplet
9      implements Runnable, TicTacToeConstants {
10    // Indicate whether the player has the turn
11    private boolean myTurn = false;
12    
13    // Indicate the token for the player
14    private char myToken = ' ';
15 
16    // Indicate the token for the other player
17    private char otherToken = ' ';
18    
19    // Create and initialize cells
20    private Cell[][] cell = new Cell[3][3];
21 
22    // Create and initialize a title label
23    private JLabel jlblTitle = new JLabel();
24  
25    // Create and initialize a status label
26    private JLabel jlblStatus = new JLabel();
27 
28    // Indicate selected row and column by the current move
29    private int rowSelected;
30    private int columnSelected;
31    
32    // Input and output streams from/to server
33    private DataInputStream fromServer;
34    private DataOutputStream toServer;
35    
36    // Continue to play?
37    private boolean continueToPlay = true;
38     
39    // Wait for the player to mark a cell
40    private boolean waiting = true;
41 
42    // Indicate if it runs as application
43    private boolean isStandAlone = false;
44 
45    // Host name or ip
46    private String host = "localhost";
47 
48    /** Initialize UI */
49    public void init() {
50      // Panel p to hold cells
51      JPanel p = new JPanel();
--------------------------------------------------------------------------------[Page 846] 52    p.setLayout(new GridLayout(3, 3, 0, 0)); 
53    for (int i = 0; i < 3; i++)
54      for (int j = 0; j < 3; j++)
55        p.add(cell[i][j] = new Cell(i, j));
56 
57    // Set properties for labels and borders for labels and panel
58     p.setBorder(new LineBorder(Color.black, 1));
59     jlblTitle.setHorizontalAlignment(JLabel.CENTER); 
60     jlblTitle.setFont(new Font("SansSerif", Font.BOLD, 16));
61     jlblTitle.setBorder(new LineBorder(Color.black, 1));
62     jlblStatus.setBorder(new LineBorder(Color.black, 1));
63 
64     // Place the panel and the labels to the applet
65     add(jlblTitle, BorderLayout.NORTH); 
66     add(p, BorderLayout.CENTER);
67     add(jlblStatus, BorderLayout.SOUTH);
68    
69     // Connect to the server
70     connectToServer();
71  } 
72  
73  private void connectToServer() {
74    try {
75      // Create a socket to connect to the server
76      Socket socket;
77      if (isStandAlone)
78        socket = new Socket(host, 8000);
79      else
80        socket = new Socket(getCodeBase().getHost(), 8000);
81 
82      // Create an input stream to receive data from the server
83      fromServer = new DataInputStream(socket.getInputStream());
84 
85      // Create an output stream to send data to the server
86      toServer = new DataOutputStream(socket.getOutputStream());
87   }
88   catch (Exception ex) {
89     System.err.println(ex);
90   }
91 
92   // Control the game on a separate thread
93   Thread thread = new Thread(this);
94   thread.start();
95  }
96 
97  public void run() {
98    try {
99      // Get notification from the server
100      int player = fromServer.readInt();
101
102      // Am I player 1 or 2?
103      if (player == PLAYER1) {
104        myToken = 'X';
105        otherToken = 'O';
106        jlblTitle.setText("Player 1 with token 'X'");
107        jlblStatus.setText("Waiting for player 2 to join");
108
109        // Receive startup notification from the server
110        fromServer.readInt(); // Whatever read is ignored
111
112        // The other player has joined
--------------------------------------------------------------------------------[Page 847]113        jlblStatus.setText("Player 2 has joined. I start first");
114
115        // It is my turn
116        myTurn = true;
117       }
118       else if (player == PLAYER2) {
119         myToken = 'O';
120         otherToken = 'X';
121         jlblTitle.setText("Player 2 with token 'O'");
122         jlblStatus.setText("Waiting for player 1 to move");
123       }
124
125       // Continue to play
126       while (continueToPlay) {
127         if (player == PLAYER1) {
128           waitForPlayerAction(); // Wait for player 1 to move
129           sendMove(); // Send the move to the server
130           receiveInfoFromServer(); // Receive info from the server
131       }
132       else if (player == PLAYER2) {
133         receiveInfoFromServer(); // Receive info from the server
134         waitForPlayerAction(); // Wait for player 2 to move
135         sendMove(); // Send player 2's move to the server
136        }
137       }
138      }
139      catch (Exception ex) {
140    }
141  }
142
143  /** Wait for the player to mark a cell */
144  Private void waitForPlayerAction() throws InterruptedException {
145    while (waiting) {
146      Thread.sleep(100);
147    }
148
149    waiting = true;
150  }
151  
152  /** Send this player's move to the server */
  private void sendMove() throws IOException {
154    toServer.writeInt(rowSelected); // Send the selected row
155    toServer.writeInt(columnSelected); // Send the selected column
156  }
157
158  /** Receive info from the server */
159  private void receiveInfoFromServer() throws IOException {
160    // Receive game status
161    int status = fromServer.readInt();
162
163    if (status == PLAYER1_WON) {
164       // Player 1 won, stop playing
165       continueToPlay = false;
166       if (myToken == 'X') {
167         jlblStatus.setText("I won! (X)");
168       }
169       else if (myToken == 'O') {
170         jlblStatus.setText("Player 1 (X) has won!");
171         receiveMove();
172       }
173     }
--------------------------------------------------------------------------------[Page 848]174     else if (status == PLAYER2_WON) {
175       // Player 2 won, stop playing
176       continueToPlay = false;
177       if (myToken == 'O') {
178         jlblStatus.setText("I won! (O)");
179       }
180       else if (myToken == 'X') {
181         jlblStatus.setText("Player 2 (O) has won!");
182          receiveMove();
183       }
184     }
185     else if (status == DRAW) {
186       // No winner, game is over
187       continueToPlay = false;
188       jlblStatus.setText("Game is over, no winner!");
189
190       if (myToken == 'O') {
191         receiveMove();
192       }
193     }
194     else {
195       receiveMove();
196       jlblStatus.setText("My turn");
197       myTurn = true; // It is my turn
198     }
199   }
200
201   private void receiveMove() throws IOException {
202     // Get the other player's move
203     int row = fromServer.readInt();
204     int column = fromServer.readInt();
205     cell[row][column].setToken(otherToken);
206   }
207   
208   // An inner class for a cell
209   public class Cell extends JPanel {
210     // Indicate the row and column of this cell in the board
211     private int row;
212     private int column;
213
214     // Token used for this cell
215     private char token = ' ';
216
217     public Cell(int row, int column) {
218       this.row = row;
219       this.column = column;
220       setBorder(new LineBorder(Color.black, 1)); // Set cell's border
221       addMouseListener(new ClickListener()); // Register listener
222     }
223
224     /** Return token */
225     public char getToken() {
226       return token;
227     }
228
229     /** Set a new token */
230     public void setToken(char c) {
231       token = c;
232       repaint();
233     }
234
--------------------------------------------------------------------------------[Page 849]235     /** Paint the cell */
236     protected void paintComponent(Graphics g) {
237       super.paintComponent(g);
238
239       if (token == 'X') {
240         g.drawLine(10, 10, getWidth() - 10, getHeight() - 10);
241         g.drawLine(getWidth() - 10, 10, 10, getHeight() - 10);
242       }
243       else if (token == 'O') {
244         g.drawOval(10, 10, getWidth() - 20, getHeight() - 20);
245       }
246     }
247
248     /** Handle mouse click on a cell */
249     private class ClickListener extends MouseAdapter {
250       public void mouseClicked(MouseEvent e) {
251         // If cell is not occupied and the player has the turn
252         if ((token == ' ') && myTurn) {
253           setToken(myToken); // Set the player's token in the cell
254           myTurn = false;
255           rowSelected = row;
256           columnSelected = column;
257           jlblStatus.setText("Waiting for the other player to move");
258           waiting = false; // Just completed a successful move
259         }
260       }
261     }
262   }
263 }


