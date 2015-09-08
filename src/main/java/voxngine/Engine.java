package voxngine;

import static org.lwjgl.glfw.GLFW.glfwDestroyWindow;
import static org.lwjgl.glfw.GLFW.glfwGetTime;
import static org.lwjgl.glfw.GLFW.glfwMakeContextCurrent;
import static org.lwjgl.glfw.GLFW.glfwPollEvents;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;
import static org.lwjgl.glfw.GLFW.glfwSwapBuffers;
import static org.lwjgl.glfw.GLFW.glfwSwapInterval;
import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwWindowShouldClose;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_NO_ERROR;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glGetError;
import static org.lwjgl.opengl.GL11.glViewport;

import org.lwjgl.glfw.Callbacks;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GLContext;

import voxngine.graphics.RenderEngine;
import voxngine.io.Controlls;
import voxngine.io.ScreenMessage;
import voxngine.io.Window;


public abstract class Engine
{
    private static boolean running;

    // The callbacks
    private GLFWErrorCallback       errorCallback;
        
    public static final int TARGET_FPS = 60;
    public static final int TARGET_UPS = 30;
    
    protected Timer timer;
	
	private RenderEngine renderer;
	private Controlls controlls;
			
    public Engine()
    {
    	
    	Window.init("Engine");
    	
        glfwMakeContextCurrent(Window.id);
        GLContext.createFromCurrent();
        glfwSwapInterval(0);
    	
        timer = new Timer();
        timer.init();

        renderer = new RenderEngine();
        controlls = new Controlls();
        
    }

    public abstract void init(RenderEngine renderer);
    
    public abstract void input(Controlls controlls);

    public abstract void update(float delta);

    public abstract void render(RenderEngine renderer2);

    public abstract void dispose();

    public void start()
    {
        float now, last, delta;
        
        last = 0;

        // Create the callbacks
        errorCallback = Callbacks.errorCallbackPrint(System.err);

        // Set the callbacks
        glfwSetErrorCallback(errorCallback);
        running = true;
        
        init(renderer);
        
        // Loop continuously and render and update
        while (running && glfwWindowShouldClose(Window.id) != GL_TRUE)
        {
        	        	
            // Get the time
            now = (float) glfwGetTime();
            delta = timer.getDelta();
            last = now;
            
            // Poll the events and swap the buffers
            glfwPollEvents();
            glfwSwapBuffers(Window.id);
            
            input(controlls);
            renderer.input(controlls);
            
            controlls.endEvents();
            
            update(delta);
            renderer.update(delta);
            timer.updateUPS();
            
            glViewport(0, 0, Window.WIDTH, Window.HEIGHT);
            // Clear the screen
           
            glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
            
            render(renderer);
            renderer.render();
            timer.updateFPS();
            
            timer.update();
            
            Window.queScreenMessage("DebugOverlay", new ScreenMessage("FPS: "+timer.getFPS()));
            
            sync(TARGET_FPS); 
                     
            int error = glGetError();
    		if (error != GL_NO_ERROR)
    			System.err.println(error);
            
        }
        
        // Dispose the game
        dispose();

        errorCallback.release();
        controlls.destroy();
        renderer.dispose();

        // Destroy the window
        glfwDestroyWindow(Window.id);
        glfwTerminate();

        System.exit(0);
    }
    
    /**
     * Synchronizes the game at specified frames per second.
     *
     * @param fps Frames per second
     */
    public void sync(int fps) {
        double lastLoopTime = timer.getLastLoopTime();
        double now = timer.getTime();
        float targetTime = 1f / fps;

        while (now - lastLoopTime < targetTime) {
            Thread.yield();

            /* This is optional if you want your game to stop consuming too much
             CPU but you will loose some accuracy because Thread.sleep(1) could
             sleep longer than 1 millisecond */
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
               System.out.println(Engine.class.getName());
            }

            now = timer.getTime();
        }
    }
    
    
    protected RenderEngine getRenderer() {
    	return this.renderer;
    }

	public void end() {
        running = false;
    }

}
