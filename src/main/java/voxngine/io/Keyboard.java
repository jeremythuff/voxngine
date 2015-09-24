package voxngine.io;

import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;

import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWKeyCallback;


public class Keyboard {
	
	private GLFWKeyCallback keyCallback;
	private boolean keyEvent = false;
	private boolean[] keys = new boolean[1000];

	Keyboard() {
		glfwSetKeyCallback(Window.id, keyCallback = new GLFWKeyCallback() {
			@Override
			public void invoke(long window, int key, int scancode, int action, int mods) {
				keys[key] = action != GLFW.GLFW_RELEASE;
				keyEvent = true;
			}
        });
	}

	public boolean isKeyDown(int keycode) {
		return keys[keycode];
	}
	
	public void init() {
		new Keyboard();
	}
	
	public void endEvents() {
    	keyEvent = false;
    }
    
    public boolean activeKeyEvent() {
    	return keyEvent;
    }
	
    public void destroy() {
    	keyCallback.release();
	}
	
}
