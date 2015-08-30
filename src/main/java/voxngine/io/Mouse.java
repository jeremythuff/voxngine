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

	private static GLFWCursorPosCallback   	cursorPosCallback;
    private static GLFWMouseButtonCallback 	mouseButtonCallback;
    private static GLFWScrollCallback  		scrollCallback;
    
    private static boolean moveEvent = false;
    private static boolean clickEvent = false;
    private static boolean scrollEvent = false;
    
    private static Vector2d mousePos = new Vector2d();
    private static Vector2d scrollDelta = new Vector2d();
    private static int action = 0;
    
    private static boolean[] buttons = new boolean[500];
	
	private Mouse() {
		
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
    
    public static Vector2d getPos() {
    	return mousePos;
    }
    
    public static Vector2d getScrollDelta() {
    	return scrollDelta;
    }
    
    public static boolean actionIs(int action) {
    	return Mouse.action==action;
    }
    
    public static boolean isButtonDown(int buttonCode) {
    	return buttons[buttonCode];
    }
    
    public static void endEvents() {
    	moveEvent = false;
    	clickEvent = false;
    	scrollEvent = false;
    }
    
    public static boolean activeMoveEvent() {
    	return moveEvent;
    }
    
    public static boolean activeClickEvent() {
    	return clickEvent;
    }
    
    public static boolean activeScrollEvent() {
    	return scrollEvent;
    }
    
    public static void destroy() {
        cursorPosCallback.release();
        mouseButtonCallback.release();
        scrollCallback.release();
	}
    
    public static void init() {
    	new Mouse();
    }
	
	
}
