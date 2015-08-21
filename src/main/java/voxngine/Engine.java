package voxngine;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.opengl.GLContext;

import voxngine.io.Window;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;

public abstract class Engine
{
    private static boolean running;

    // The callbacks
    private GLFWErrorCallback       errorCallback;
    private GLFWKeyCallback         keyCallback;
    private GLFWCursorPosCallback   cursorPosCallback;
    private GLFWMouseButtonCallback mouseButtonCallback;
    private GLFWScrollCallback      scrollCallback;
    
    private static Window window;
    
    public static double timer = 0;
	public static int frames = 0;
	public static int fps;
	
	public static double lastX = 0, lastY = 0;
	
	public int fontHeight;
	
    public Engine()
    {
    	window = new Window("Engine");
        glfwMakeContextCurrent(window.id);
        GLContext.createFromCurrent();
        glfwSwapInterval(1);
    }

    public abstract void init();

    public abstract void update(float delta);

    public abstract void render(float delta);

    public abstract void dispose();

    public void start()
    {
        float now, last, delta;
        
        last = 0;

        // Create the callbacks
        errorCallback = Callbacks.errorCallbackPrint(System.err);
        keyCallback = GLFWKeyCallback(this::glfwKeyCallback);
        cursorPosCallback = GLFWCursorPosCallback(this::glfwCursorPosCallback);
        mouseButtonCallback = GLFWMouseButtonCallback(this::glfwMouseButtonCallback);
        scrollCallback = GLFWScrollCallback(this::glfwScrollCallback);

        // Set the callbacks
        glfwSetErrorCallback(errorCallback);
        glfwSetKeyCallback(window.id, keyCallback);
        glfwSetCursorPosCallback(window.id, cursorPosCallback);
        glfwSetMouseButtonCallback(window.id, mouseButtonCallback);
        glfwSetScrollCallback(window.id, scrollCallback);
                
        int imageWidth = 0;
        int imageHeight = 0;



        fontHeight = imageHeight;

        running = true;
        
        init();

        // Loop continuously and render and update
        while (running && glfwWindowShouldClose(window.id) != GL_TRUE)
        {
        	        	
            // Get the time
            now = (float) glfwGetTime();
            delta = now - last;
            last = now;
            
            
            update(delta);
            render(delta);
            frames ++;
            
            if (glfwGetTime() - timer > 1) {
				timer += 1;
		        fps = frames;
				frames = 0;
			}
            
            int error = glGetError();
    		if (error != GL_NO_ERROR)
    			System.err.println(error);
    		
            // Poll the events and swap the buffers
            glfwPollEvents();
            glfwSwapBuffers(window.id);
            
        }

        // Dispose the game
        dispose();

        // Release the callbacks
        keyCallback.release();
        cursorPosCallback.release();
        mouseButtonCallback.release();
        scrollCallback.release();
        errorCallback.release();

        // Destroy the window
        glfwDestroyWindow(window.id);
        glfwTerminate();

        System.exit(0);
    }

	public void end() {
        running = false;
    }

    // Callback functions which can be overriden
    public void glfwKeyCallback(long window, int key, int scancode, int action, int mods) {
        // End on escape
        if (key == GLFW_KEY_ESCAPE && action != GLFW_RELEASE)
            end();
    }

    public void glfwCursorPosCallback(long window, double xpos, double ypos) {
       	
    	lastX = xpos;
    	lastY = ypos;
    	
    }

    public void glfwMouseButtonCallback(long window, int button, int action, int mods) {
    	
    }

    public void glfwScrollCallback(long window, double xoffset, double yoffset) {
    }

    // Static helpful polled input methods
    public static boolean isKeyPressed(int key) {
        return glfwGetKey(window.id, key) != GLFW_RELEASE;
    }

    public static boolean isMouseButtonPressed(int button) {
        return glfwGetMouseButton(window.id, button) != GLFW_RELEASE;
    }

    public static long getWindowID() {
        return window.id;
    }
    
    public long getTime() {
        return System.nanoTime() / 1000000;
    }
    
}
