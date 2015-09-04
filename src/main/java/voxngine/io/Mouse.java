package voxngine.io;

import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;

import org.joml.Vector2d;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

public class Mouse {

	private GLFWCursorPosCallback   	cursorPosCallback;
    private GLFWMouseButtonCallback 	mouseButtonCallback;
    private GLFWScrollCallback  		scrollCallback;
    
    private boolean moveEvent = false;
    private boolean clickEvent = false;
    private boolean scrollEvent = false;
    
    private Vector2d mousePos = new Vector2d();
    private Vector2d scrollDelta = new Vector2d();
    private static int action = 0;
    
    private boolean[] buttons = new boolean[500];
	
	Mouse() {
		
		glfwSetCursorPosCallback(Window.id, cursorPosCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
            	mousePos.set(xpos, ypos);
            	moveEvent = true;
            }
        });
        glfwSetMouseButtonCallback(Window.id, mouseButtonCallback = new GLFWMouseButtonCallback() {  		
            @Override
            public void invoke(long window, int button, int action, int mods) {            	
            	Mouse.action = action;
            	buttons[button] = action != GLFW.GLFW_RELEASE;
            	clickEvent = true;
            }
        });
        glfwSetScrollCallback(Window.id, scrollCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
            	scrollDelta.set(xoffset, yoffset);
            	scrollEvent = true;
            }
        });
	}
    
    public Vector2d getPos() {
    	return mousePos;
    }
    
    public Vector2d getScrollDelta() {
    	return scrollDelta;
    }
    
    public boolean actionIs(int action) {
    	return Mouse.action==action;
    }
    
    public boolean isButtonDown(int buttonCode) {
    	return buttons[buttonCode];
    }
    
    public void endEvents() {
    	moveEvent = false;
    	clickEvent = false;
    	scrollEvent = false;
    }
    
    public boolean activeMoveEvent() {
    	return moveEvent;
    }
    
    public boolean activeClickEvent() {
    	return clickEvent;
    }
    
    public boolean activeScrollEvent() {
    	return scrollEvent;
    }
    
    public void destroy() {
        cursorPosCallback.release();
        mouseButtonCallback.release();
        scrollCallback.release();
	}
    
    public  void init() {
    	new Mouse();
    }
	
	
}
