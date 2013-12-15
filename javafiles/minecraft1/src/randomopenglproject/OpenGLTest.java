package randomopenglproject;


import javax.swing.SwingUtilities;


/**
 * To build using maven:
 *
 * Download lwjgl-maven-2.7.1.zip and install the jars into your local maven
 * repository and then do a command-line build.
 *
 * See the README.txt for more details.
 *
 * To execute in the IDE add the path to the lwjgl native libraries as a
 * VM parameter in the Run configuration:
 *
 * -Djava.library.path=target/lib/natives
 */
public class OpenGLTest {

    public static void main(String[] args) {
        // On macs, use screen menubar instead of a menu inside the frame
        System.setProperty("apple.laf.useScreenMenuBar", "true");

        // Apple Application Menu
        final String APP_TITLE = "OpenGL Test";
        System.setProperty("com.apple.mrj.application.apple.menu.about.name", APP_TITLE);

        try {
            SwingUtilities.invokeAndWait(new Runnable() {
                public void run() {
                    GLFrame glFrame = new GLFrame();
                    glFrame.setTitle(APP_TITLE);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
