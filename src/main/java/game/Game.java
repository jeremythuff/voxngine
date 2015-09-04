package game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;
import static org.lwjgl.opengl.GL11.GL_COLOR_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.GL_DEPTH_BUFFER_BIT;
import static org.lwjgl.opengl.GL11.glClear;
import static org.lwjgl.opengl.GL11.glClearColor;

import java.util.ArrayList;
import java.util.List;

import game.gui.Gui;
import game.world.World;
import voxngine.Engine;
import voxngine.graphics.RenderEngine;
import voxngine.io.Controlls;
import voxngine.io.Keyboard;
import voxngine.io.Window;

public class Game extends Engine {
	
	protected RenderEngine renderer;
	private List<GameObject> gameObjecs = new ArrayList<GameObject>();
				
	public static void main(String[] args) {
        new Game().start();
    }

	@Override
	public void init(RenderEngine renderer) {
		
		this.renderer = renderer;
		
		glfwSetWindowTitle(Window.id, "Voxel Game");
		
		System.out.println("Game Started...");
		
		gameObjecs.add(new World());
		gameObjecs.add(new Gui());
		
		for(GameObject gameObjec : gameObjecs) {
			gameObjec.init(renderer);
		}
				
		System.out.println("Initialization Done!\n");
	}
	
	@Override
	public void input(Controlls controlls) {
		
		if(controlls.getKeyboad().activeKeyEvent()) {
			if(controlls.getKeyboad().isKeyDown(GLFW_KEY_ESCAPE)) {
				end();
			}
		}
		
		for(GameObject gameObjec : gameObjecs) {
			gameObjec.input(controlls);
		}
	}

	@Override
	public void update(float delta) {
		for(GameObject gameObjec : gameObjecs) {
			gameObjec.update(delta);
		}
	}

	@Override
	public void render(RenderEngine renderer) {
	
		glClearColor(0.9f, 0.9f, 0.9f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		for(GameObject gameObjec : gameObjecs) {
			gameObjec.render(renderer);
		}
	}
	
	@Override
	public void dispose() {
		System.out.println("Shutting down...");
		for(GameObject gameObjec : gameObjecs) {
			gameObjec.dispose();
		}
		System.out.println("Game Ended!");
	}
	
}
