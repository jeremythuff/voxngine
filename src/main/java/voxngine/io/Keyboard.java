package voxngine.io;

import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;


public class Keyboard {
	
	private static GLFWKeyCallback keyCallback;
	
	public static boolean[] keys = new boolean[65536];

	private Keyboard() {
		glfwSetKeyCallback(Window.id, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				keys[key] = action != GLFW.GLFW_RELEASE; 
			}
        });
	}

	public static boolean isKeyDown(int keycode) {
		return keys[keycode];
	}
	
	public static void init() {
		new Keyboard();
	}
	
    public static void destroy() {
    	keyCallback.release();
	}
	
}
