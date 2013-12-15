package randomopenglproject;


import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;

import org.lwjgl.LWJGLException;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import static org.lwjgl.opengl.GL11.*;


public class GLCanvas extends Canvas {

    public Label renderLabel = new Label();
    public Label statusLabel = new Label();

    private boolean isInitialized = false;

    public GLCanvas() {
        setFocusable(true);
        setVisible(true);

        renderLabel.setPreferredSize(new Dimension(100, 20));
        statusLabel.setPreferredSize(new Dimension(100, 20));

        addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent componentEvent) {
                if (isInitialized) {
                    glViewport(0, 0, getWidth(), getHeight());
                }
            }
        });
    }

    public void initialize() {
        try {
            Display.setParent(this);
            Display.setFullscreen(false);
            Display.create();

            glViewport(0, 0, getWidth(), getHeight());
            isInitialized = true;
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public final void removeNotify() {
        if (isInitialized) {
            Display.destroy();
        }
        super.removeNotify();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);

        if (!isInitialized) {
            if (isDisplayable()) {
                initialize();
            }
        }

        long start_ms = System.currentTimeMillis();

        preRender();
        render();

        Display.update();

        Long elapsed = System.currentTimeMillis() - start_ms;
        String last_3_digits = String.format("%.3f", ((start_ms % 1000) / 1000.0));
        renderLabel.setText("start time " + last_3_digits + " (" + elapsed + " ms render)");

        Display.sync(60);  // fps
    }

    //////////////////////////
    // OpenGL - handleInput //
    //////////////////////////

    public void handleInput() {
        if (!isInitialized) {
            return;
        }

        // standard mouse and keyboard listeners on the lwjgl canvas are not
        // reliable from os to os on the lwjgl canvas

        int wheel = Mouse.getDWheel();
        if (wheel != 0) {
            statusLabel.setText("Last input - lwjgl wheel: " + wheel);
            System.out.println("lwjgl wheel: " + wheel);
        }

        //System.out.println("mouse move: " + Mouse.getX() + "," + Mouse.getY());

        if (Mouse.isButtonDown(0)) {
            statusLabel.setText("Last input - lwjgl left button " + Mouse.getX() + "," + Mouse.getY());
            System.out.println("Last input - lwjgl left button " + Mouse.getX() + "," + Mouse.getY());
        }
        if (Mouse.isButtonDown(1)) {
            statusLabel.setText("Last input - lwjgl right button " + Mouse.getX() + "," + Mouse.getY());
            System.out.println("Last input - lwjgl right button " + Mouse.getX() + "," + Mouse.getY());
        }
        if (Mouse.isButtonDown(2)) {
            statusLabel.setText("Last input - lwjgl middle button " + Mouse.getX() + "," + Mouse.getY());
            System.out.println("Last input - lwjgl middle button " + Mouse.getX() + "," + Mouse.getY());
        }

        while (Keyboard.next()) {
            int key = Keyboard.getEventKey();

            if (Keyboard.getEventKeyState()) {
                statusLabel.setText("Last input - lwjgl key pressed: " + key);
                System.out.println("lwjgl key pressed - Keyboard.getEventKey(): " + key);
            } else {
                statusLabel.setText("Last input - lwjgl key released: " + key);
                System.out.println("lwjgl key released - Keyboard.getEventKey(): " + key);
            }

        }
    }

    ////////////////////////////////
    // OpenGL - preRender, render //
    ////////////////////////////////

    private void preRender() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);

        glMatrixMode(GL_PROJECTION);
        glLoadIdentity();

        glMatrixMode(GL_MODELVIEW);
        glLoadIdentity();
    }

    private void render() {
        glClearColor(0, 0, 0, 1);  // black

        glBegin(GL_TRIANGLES);

        glColor3f(1, 0, 0);
        glVertex3f(-0.5f, -0.5f, 0.0f);

        glColor3f(0, 0.5f, 0);

        float partial = new Double((System.currentTimeMillis() % 1000) / 1000.0).floatValue();
        glVertex3f(partial, -0.5f, 0.0f);

        glColor3f(0, 0, 1);
        glVertex3f(+0.5f, +0.5f, 0.0f);

        glEnd();
    }
}
