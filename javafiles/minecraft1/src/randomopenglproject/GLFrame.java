package randomopenglproject;


import java.awt.*;
import java.awt.event.*;

import javax.swing.Timer;


/**
 * Don't mix Swing and AWT components in the Frame that contains the lwjgl
 * Canvas.
 */
public class GLFrame extends Frame {

    private GLCanvas glCanvas = new GLCanvas();
    private Timer timer;

    public GLFrame() {

        registerForMacOSXEvents();

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                dispose();
            }
        });

        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Debug");
        for (int i = 0;  i < 15;  i++) {
            menu.add(new MenuItem("Null Item " + i));
        }
        menuBar.add(menu);
        setMenuBar(menuBar);

        Panel simple = new Panel() {
            @Override
            public void paint(Graphics g) {
                g.drawString("Left", 5, 50);
                g.drawString("Panel", 5, 70);
            }
        };
        simple.setPreferredSize(new Dimension(50, 0));
        simple.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent mouseEvent) {
                glCanvas.statusLabel.setText("Last input - click on West Panel " + mouseEvent.getPoint());
                System.out.println("click on West Panel " + mouseEvent.getPoint());
            }
        });

        Button button = new Button("AWTButton");
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                glCanvas.statusLabel.setText("Last input - AWTButton click");
                System.out.println("AWTButton click");
            }
        });

        TextArea ta = new TextArea();
        ta.setEnabled(false);
        ta.setText("Verify the following (there is a status bar on the bottom):\n" +
                "* You should see an animated triangle in the center OpenGL panel\n" +
                "* Mouse presses in the left Panel are captured by a MouseListener\n" +
                "* Mouse presses in the center are captured by lwjgl\n" +
                "* Mouse wheels while the cursor is in the center only are captured by lwjgl\n" +
                "* Click in the center to gain focus and then key presses are captured by lwjgl\n" +
                "* The Button on the right is clickable\n" +
                "* The Debug menu should not be hidden by the lwjgl canvas\n" +
                "* The window is resizable");

        Panel labels = new Panel(new GridLayout(2, 1));
        labels.add(glCanvas.statusLabel);
        labels.add(glCanvas.renderLabel);

        Panel panel = new Panel(new BorderLayout());
        panel.add(ta, BorderLayout.NORTH);
        panel.add(simple, BorderLayout.WEST);
        panel.add(button, BorderLayout.EAST);
        panel.add(labels, BorderLayout.SOUTH);

        panel.add(glCanvas, BorderLayout.CENTER);

        // Note that you with lwjgl 2.7.1 you cannot add a listener to the
        // canvas because it will not work on Windows.
        //
        // "Java uses the GWLP_USERDATA field to store a pointer to an
        // AWTComponent object, which apparently is required so the Canvas
        // can cooperate with AWT correctly. SFML [and lwjgl on Windows only]
        // overrides that field to store its own Window object pointer, which
        // will of course break AWT functionality."
        //
        // http://www.sfml-dev.org/forum/viewtopic.php?t=4938&postdays=0&postorder=asc&start=15

        add(panel);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent windowEvent) {
                quit();
            }
        });

        setPreferredSize(new Dimension(500, 400));
        pack();
        setVisible(true);

        // Passive rendering on the EDT.
        // Choose an arbitrary interval but faster than 60 fps.
        timer = new Timer(10, new ActionListener() {
            public void actionPerformed(ActionEvent actionEvent) {
                glCanvas.repaint();
                glCanvas.handleInput();
            }
        });
        timer.start();
    }

    /**
     * General quit handler; fed to the OSXAdapter as the method to call when
     * a system quit event occurs.
     *
     * On a Mac a quit event is triggered by Cmd-Q, selecting Quit from the
     * application or Dock menu, or logging out.
     *
     * @return true if we should quit
     */
    boolean quit() {
        System.out.println("GLFrame.quit()");

        timer.stop();
        dispose();

        return true;
    }

    private boolean isMac() {
        String osName = System.getProperty("os.name");
        return osName.toLowerCase().startsWith("mac os x");
    }

    private void registerForMacOSXEvents() {
        if (isMac()) {
            try {
                // Generate and register the OSXAdapter, passing it a hash of all the methods we wish to
                // use as delegates for various com.apple.eawt.ApplicationListener methods
                OSXAdapter.setQuitHandler(this, getClass().getDeclaredMethod("quit", (Class[])null));
                //OSXAdapter.setAboutHandler(this, getClass().getDeclaredMethod("about", (Class[])null));
                //OSXAdapter.setPreferencesHandler(this, getClass().getDeclaredMethod("preferences", (Class[])null));
                //OSXAdapter.setFileHandler(this, getClass().getDeclaredMethod("loadImageFile", new Class[] { String.class }));
            } catch (Exception e) {
                System.err.println("Error while loading the OSXAdapter:");
                e.printStackTrace();
            }
        }
    }
}
