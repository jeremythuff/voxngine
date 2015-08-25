package voxngine;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;
import org.lwjgl.opengl.GLContext;

import voxngine.graphics.RenderEngine;
import voxngine.graphics.shaders.ShaderProgram;
import voxngine.io.Window;

import static org.lwjgl.glfw.GLFW.*;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;


public abstract class Engine
{
    private static boolean running;

    // The callbacks
    private GLFWErrorCallback       errorCallback;
    private GLFWKeyCallback         keyCallback;
    private GLFWCursorPosCallback   cursorPosCallback;
    private GLFWMouseButtonCallback mouseButtonCallback;
    private GLFWScrollCallback      scrollCallback;
        
    public double timer = 0;
	public int frames = 0;
	public int fps;
		
	public double lastX = 0, lastY = 0;
	
	public RenderEngine renderEngine;
		
    public Engine()
    {
    	Window.init("Engine");
        glfwMakeContextCurrent(Window.id);
        GLContext.createFromCurrent();
        glfwSwapInterval(0);
        
        renderEngine = new RenderEngine();
        
    }

    public abstract void init();
    
    public abstract void input();

    public abstract void update(float delta);

    public abstract void render(RenderEngine renderEngine2);

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
        glfwSetKeyCallback(Window.id, keyCallback);
        glfwSetCursorPosCallback(Window.id, cursorPosCallback);
        glfwSetMouseButtonCallback(Window.id, mouseButtonCallback);
        glfwSetScrollCallback(Window.id, scrollCallback);
        running = true;
        
        init();

        // Loop continuously and render and update
        while (running && glfwWindowShouldClose(Window.id) != GL_TRUE)
        {
        	        	
            // Get the time
            now = (float) glfwGetTime();
            delta = now - last;
            last = now;
            
            input();
            update(delta);
         // Clear the screen
            glClear(GL_COLOR_BUFFER_BIT);
            render(renderEngine);
            
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
            glfwSwapBuffers(Window.id);
            
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
        glfwDestroyWindow(Window.id);
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
        return glfwGetKey(Window.id, key) != GLFW_RELEASE;
    }

    public static boolean isMouseButtonPressed(int button) {
        return glfwGetMouseButton(Window.id, button) != GLFW_RELEASE;
    }

}
