package remotecontrol2;
import java.io.*;
import java.net.*;
3  import javax.swing.*;
4  import java.awt.*;
5  import java.util.Date;
6  
7  public class TicTacToeServer extends JFrame
8      implements TicTacToeConstants {
9    public static void main(String[] args) {
10      TicTacToeServer frame = new TicTacToeServer();
1    }
2 
3    public TicTacToeServer() {
4      JTextArea jtaLog = new JTextArea();
5 
6      // Create a scroll pane to hold text area
7      JScrollPane scrollPane = new JScrollPane(jtaLog);
8  
9      // Add the scroll pane to the frame
20      add(scrollPane, BorderLayout.CENTER);
2  
--------------------------------------------------------------------------------[Page 84] 22      setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
23      setSize(300, 300);
24      setTitle("TicTacToeServer");
25      setVisible(true);
26  
27      try {
28        // Create a server socket
29        ServerSocket serverSocket = new ServerSocket(8000);
30        jtaLog.append(new Date() +
3          ": Server started at socket 8000\n");
32  
33        // Number a session
34        int sessionNo = 1;
35  
36        // Ready to create a session for every two players
37        while (true) {
38          jtaLog.append(new Date() +
39            ": Wait for players to join session " + sessionNo + '\n');
40   
4             // Connect to player1 
42             Socket player1 = serverSocket.accept();
43  
44             jtaLog.append(new Date() + ": Player  1  joined session " +
45              sessionNo + '\n');
--------------------------------------------------------------------------------[Page 842] 46              jtaLog.append("Player 1's IP address" +
47                player1.getInetAddress().getHostAddress() + '\n');
48  
49              // Notify that the player is Player 1
50              new DataOutputStream(
5                player1.getOutputStream()).writeInt(PLAYER1);
52  
53              // Connect to player 2
54              Socket player2 = serverSocket.accept();
55  
56              jtaLog.append(new Date() +
57                ": Player  2  joined session " + sessionNo + '\n');
58              jtaLog.append("Player 2's IP address" +
59                player2.getInetAddress().getHostAddress() + '\n');
60  
6              // Notify that the player is Player 2
62              new DataOutputStream(
63                player2.getOutputStream()).writeInt(PLAYER2);
64  
65              // Display this session and increment session number
66              jtaLog.append(new Date() + ": Start a thread for session " +
67                sessionNo++ + '\n');
68  
69              // Create a new thread for this session of two players
70              HandleASession task = new HandleASession(player1, player2);
71  
72              // Start the new thread
73              new Thread(task).start();
74           }  
75        }  
76        catch(IOException ex) {
78          System.err.println(ex);
79        } 
80     }
81  }
82  
83  // Define the thread class for handling a new session for two players
84  class HandleASession implements Runnable, TicTacToeConstants { 
85    private Socket player1;
86    private Socket player2;
87  
88    // Create and initialize cells
89    private char[][] cell = new char[3][3];
90  
91    private DataInputStream fromPlayer1;
92    private DataOutputStream toPlayer1;
93    private DataInputStream fromPlayer2;
94    private DataOutputStream toPlayer2;
95  
96    // Continue to play
97    private boolean continueToPlay = true;
98  
99    /** Construct a thread */
100    public HandleASession(Socket player1, Socket player2) {
101      this.player1 = player1;
102      this.player2 = player2;
103
104      // Initialize cells
105      for (int i = 0; i < 3; i++)
106        for (int j = 0; j < 3; j++)
107          cell[i][j] = ' ';
--------------------------------------------------------------------------------[Page 843]108      }
109
110      /** Implement the run() method for the thread */
111      public void run() {
112        try {
113          // Create data input and output streams
114          DataInputStream fromPlayer1  = new DataInputStream(
115            player1.getInputStream());
116          DataOutputStream toPlayer1  = new DataOutputStream(
117            player1.getOutputStream());
118          DataInputStream fromPlayer2  = new DataInputStream(
119            player2.getInputStream());
120          DataOutputStream toPlayer2  = new DataOutputStream(
121            player2.getOutputStream()); 
122
123          // Write anything to notify player  1  to start
124          // This is just to let player  1  know to start
125          toPlayer1.writeInt(1);
126
127          // Continuously serve the players and determine and report
128          // the game status to the players
129          while (true) {
130            // Receive a move from player 1
131            int row = fromPlayer1.readInt();
132            int column = fromPlayer1.readInt();
133            cell[row][column] = 'X';
134
135            // Check if Player  1  wins
136            if (isWon('X')) {
137              toPlayer1.writeInt(PLAYER1_WON);
138              toPlayer2.writeInt(PLAYER1_WON);
139              sendMove(toPlayer2, row, column);
140              break; // Break the loop
141            }
142            else if (isFull()) { // Check if all cells are filled
143              toPlayer1.writeInt(DRAW);
144              toPlayer2.writeInt(DRAW);
145              sendMove(toPlayer2, row, column);
146              break;
147            }
148            else {
149              // Notify player  2  to take the turn
150              toPlayer2.writeInt(CONTINUE);
151
152              // Send player 1's selected row and column to player 2
153              sendMove(toPlayer2, row, column);
154          }
155
156           // Receive a move from Player 2
157           row = fromPlayer2.readInt();
158           column = fromPlayer2.readInt();
159           cell[row][column] = 'O';
160 
161           // Check if Player  2  wins
162           if (isWon('O')) {
163             toPlayer1.writeInt(PLAYER2_WON);
164             toPlayer2.writeInt(PLAYER2_WON);
165             sendMove(toPlayer1, row, column);
166             break;
167           }
168           else {
--------------------------------------------------------------------------------[Page 844]169           // Notify player  1  to take the turn
170           toPlayer1.writeInt(CONTINUE);
171   
172           // Send player 2's selected row and column to player 1
173           sendMove(toPlayer1, row, column);
174         } 
175      }  
176    } 
177    catch(IOException ex) {
178      System.err.println(ex);
179    }
180  }
181  
182  /** Send the move to other player */
183  private void sendMove(DataOutputStream out, int row, int column)
184      throws IOException {
185    out.writeInt(row); // Send row index
186    out.writeInt(column); // Send column index
187  }
188
189  /** Determine if the cells are all occupied */
190  private boolean isFull() {
191    for (int i = 0; i < 3; i++)
192      for (int j = 0; j < 3; j++)
193        if (cell[i][j] == ' ')
194           return false; // At least one cell is not filled
195
196    // All cells are filled
197    return true;
198  }
199
200  /** Determine if the player with the specified token wins */
201  private boolean isWon(char token) {
202    // Check all rows
203    for (int i = 0; i < 3; i++) 
204      if ((cell[i][0] == token)
205          && (cell[i][1] == token)
206          && (cell[i][2] == token)) {
207        return true;
208      }
209
210     /** Check all columns */
211     for (int j = 0; j < 3; j++)
212       if ((cell[0][j] == token)
213           && (cell[1][j] == token)
214           && (cell[2][j] == token)) {
215         return true;
216       }
217      
218     /** Check major diagonal */
219     if ((cell[0][0] == token)
220         && (cell[1][1] == token)
221         && (cell[2][2] == token)) {
222       return true;
223     }
224
225     /** Check subdiagonal */
226     if ((cell[0][2] == token)
227         && (cell[1][1] == token)
228         && (cell[2][0] == token)) {
--------------------------------------------------------------------------------[Page 845]229       return true;
230     }
231
232     /** All checked, but no winner */
233     return false;
234   }
235  } 


