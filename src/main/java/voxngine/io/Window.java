package voxngine.io;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.GL_FALSE;
import static org.lwjgl.opengl.GL11.GL_TRUE;
import static org.lwjgl.system.MemoryUtil.NULL;

import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.lwjgl.glfw.GLFWvidmode;
//import org.lwjgl.opengl.ContextCapabilities;
//import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLContext;

public class Window {
	
	public static long id;
	
	public static Map<String, List<ScreenMessage>> screenMessageQue = new HashMap<String, List<ScreenMessage>>();
	
	public static int WIDTH = 1280;
	public static int HEIGHT = 720;

    private boolean vsync = true;
	
	private Window(String name) {
		
		 if (glfwInit() != GL_TRUE)
        {
            System.err.println("Error initializing GLFW");
            System.exit(1);
	    }
		 
		 /* Creating a temporary window for getting the available OpenGL version */
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
		long temp = glfwCreateWindow(1, 1, "", NULL, NULL);
		
		glfwMakeContextCurrent(temp);
		GLContext.createFromCurrent();
		
		//ContextCapabilities caps = GL.getCapabilities();
		glfwDestroyWindow(temp);
		
		 /* Reset and set window hints */
		glfwDefaultWindowHints();
		glfwWindowHint(GLFW_DEPTH_BITS, 32);
		
        //if (caps.OpenGL32) {
            /* Hints for OpenGL 3.2 core profile */
            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
            glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
            glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
//        } else if (caps.OpenGL21) {
//            /* Hints for legacy OpenGL 2.1 */
//            glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 2);
//            glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 1);
//        } else {
//            throw new RuntimeException("Neither OpenGL 3.2 nor OpenGL 2.1 is "
//                    + "supported, you may want to update your graphics driver.");
//        }
        glfwWindowHint(GLFW_RESIZABLE, GL_TRUE);
        glfwWindowHint(GLFW_SAMPLES, 8);
		
        /* Create window with specified OpenGL context */
        id = glfwCreateWindow(WIDTH, HEIGHT, name, NULL, NULL);
        if (id == NULL) {
            glfwTerminate();
            throw new RuntimeException("Failed to create the GLFW window!");
        }

        /* Enable v-sync */
        if (vsync) {
            glfwSwapInterval(1);
        }
	
		ByteBuffer vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
			glfwSetWindowPos(id, (GLFWvidmode.width(vidmode) - WIDTH) / 2, (GLFWvidmode.height(vidmode) - HEIGHT) / 2);
	}
	
	public static void queScreenMessage(String destination, ScreenMessage screenMessage) {
		
		if(screenMessageQue.containsKey(destination)) {
			screenMessageQue.get(destination).add(screenMessage);
		} else {	
			List<ScreenMessage> newMessageList = new ArrayList<ScreenMessage>();
			newMessageList.add(screenMessage);
			screenMessageQue.put(destination, newMessageList);
		}
	}
	
	public static List<ScreenMessage> getScreenMessages(String destination) {
		return screenMessageQue.get(destination);
	}
	
	public static ScreenMessage popMessage(String destination) {
		
		for(ScreenMessage screenMassage : getScreenMessages(destination)) {
			getScreenMessages(destination).remove(screenMassage);
			return screenMassage;
		}
		
		return null;
	}
	
	public static void init(String name) {
		new Window(name);
	}
	
}
