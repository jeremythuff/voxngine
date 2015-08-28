package voxngine.camera;

import org.joml.camera.ArcBallCamera;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

public class TPCamera extends AbstractCamera {
	
	ArcBallCamera cam = new ArcBallCamera();
	
	GLFWKeyCallback keyCallback;
	GLFWFramebufferSizeCallback fbCallback;
	GLFWCursorPosCallback cpCallback;
	GLFWScrollCallback sCallback;
	GLFWMouseButtonCallback mbCallback;
	
	long window;
	int x, y;
	float zoom = 20;
	int mouseX, mouseY;
	boolean down;
	
	

}
