package engine;

import files.Loader;
import graphics.FontContainer;
import graphics.SpriteContainer;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import static org.lwjgl.opengl.GL11.*;

public class Game {

    public static final int SPEED = 60;
    public static final int DISPLAY_WIDTH = 400;
    public static final int DISPLAY_HEIGHT = 400;

    private long lastFrame;
    private int fps;
    private long lastFPS;
    public Room room;

    public void destroy() {
        Mouse.destroy();
        Keyboard.destroy();
        Display.destroy();
    }

    public void init() throws LWJGLException {
        //Display
        setDisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT, false);
        Display.setVSyncEnabled(true);
        //Display.setResizable(true);
        Display.setTitle("So how are you today?");
        Display.create();
        //Keyboard
        Keyboard.create();
        //Mouse
        Mouse.create();
        //OpenGL
        initGL();
        //Sprites
        SpriteContainer.create();
        FontContainer.create();

        room = Loader.loadImage("room1");
    }

    public void initGL() {
        glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);

        glClearColor(0, 0, 0, 1); // Set background color to black and opaque
        glClearDepth(1);                   // Set background depth to farthest
        glEnable(GL_DEPTH_TEST);   // Enable depth testing for z-culling
        glDepthFunc(GL_LEQUAL);    // Set the type of depth-test
        glShadeModel(GL_SMOOTH);   // Enable smooth shading
        //glHint(GL_PERSPECTIVE_CORRECTION_HINT, GL_NICEST);  // Nice perspective corrections
    }

    public int getDelta() {
        long time = getTime();
        int delta = (int) (time - lastFrame);
        lastFrame = time;
        return delta;
    }

    public long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    public void run() {
        getDelta();
        lastFPS = getTime();
        while (!Display.isCloseRequested() && !Keys.pressed(Keyboard.KEY_ESCAPE)) {
            int delta = getDelta();
            step(delta);
            Display.update();
            Display.sync(SPEED);
        }
    }

    public void setDisplayMode(int width, int height, boolean fullscreen) {
        // return if requested DisplayMode is already set
        if ((Display.getDisplayMode().getWidth() == width)
                && (Display.getDisplayMode().getHeight() == height)
                && (Display.isFullscreen() == fullscreen)) {
            return;
        }
        try {
            DisplayMode targetDisplayMode = null;
            if (fullscreen) {
                DisplayMode[] modes = Display.getAvailableDisplayModes();
                int freq = 0;
                for (DisplayMode current : modes) {
                    if ((current.getWidth() == width) && (current.getHeight() == height)) {
                        if ((targetDisplayMode == null) || (current.getFrequency() >= freq)) {
                            if ((targetDisplayMode == null) || (current.getBitsPerPixel() > targetDisplayMode.getBitsPerPixel())) {
                                targetDisplayMode = current;
                                freq = targetDisplayMode.getFrequency();
                            }
                        }
                        // if we've found a match for bpp and frequence against the 
                        // original display mode then it's probably best to go for this one
                        // since it's most likely compatible with the monitor
                        if ((current.getBitsPerPixel() == Display.getDesktopDisplayMode().getBitsPerPixel())
                                && (current.getFrequency() == Display.getDesktopDisplayMode().getFrequency())) {
                            targetDisplayMode = current;
                            break;
                        }
                    }
                }
            } else {
                targetDisplayMode = new DisplayMode(width, height);
            }
            if (targetDisplayMode == null) {
                System.out.println("Failed to find value mode: " + width + "x" + height + " fs=" + fullscreen);
                return;
            }
            Display.setDisplayMode(targetDisplayMode);
            Display.setFullscreen(fullscreen);
        } catch (LWJGLException e) {
            System.out.println("Unable to setup mode " + width + "x" + height + " fullscreen=" + fullscreen + e);
        }
    }

    public void step(int delta) {
        Keys.update();
        MouseInput.update();
        if (Keys.clicked(Keyboard.KEY_F11)) {
            setDisplayMode(DISPLAY_WIDTH, DISPLAY_HEIGHT, !Display.isFullscreen());
        }
        room.update();
        room.draw();
        updateFPS();
    }

    public void updateFPS() {
        if (getTime() - lastFPS > 1000) {
            Display.setTitle("FPS: " + fps);
            fps = 0;
            lastFPS += 1000;
        }
        fps++;
    }
}
