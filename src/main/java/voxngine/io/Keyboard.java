package voxngine.io;

import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;


public class Keyboard {
	
	private static GLFWKeyCallback keyCallback;
	private static boolean keyEvent = false;
	private static boolean[] keys = new boolean[65536];

	private Keyboard() {
		glfwSetKeyCallback(Window.id, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				keys[key] = action != GLFW.GLFW_RELEASE;
				keyEvent = true;
			}
        });
	}

	public static boolean isKeyDown(int keycode) {
		return keys[keycode];
	}
	
	public static void init() {
		new Keyboard();
	}
	
	public static void endEvents() {
    	keyEvent = false;
    }
    
    public static boolean activeKeyEvent() {
    	return keyEvent;
    }
	
    public static void destroy() {
    	keyCallback.release();
	}
	
}
