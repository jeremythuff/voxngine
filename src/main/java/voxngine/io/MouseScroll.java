package voxngine.io;

import org.lwjgl.glfw.GLFWScrollCallback;

//Our MouseHandler class extends the abstract class
//abstract classes should never be instantiated so here
//we create a concrete that we can instantiate
public class MouseScroll extends GLFWScrollCallback {

	@Override
	public void invoke(long window, double xpos, double ypos) {
		// TODO Auto-generated method stub
		// this basically just prints out the X and Y coordinates 
		// of our mouse whenever it is in our window
		System.out.println("X: " + xpos + " Y: " + ypos);
	}	
}
