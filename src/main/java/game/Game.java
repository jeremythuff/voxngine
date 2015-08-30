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
import voxngine.io.Keyboard;
import voxngine.io.Window;

public class Game extends Engine {
	
	private List<GameObject> gameObjecs = new ArrayList<GameObject>();
				
	public static void main(String[] args) {
        new Game().start();
    }

	@Override
	public void init() {
		
		glfwSetWindowTitle(Window.id, "Voxel Game");
		
		System.out.println("Game Started...");
		
		gameObjecs.add(new World());
		gameObjecs.add(new Gui());
		
		for(GameObject gameObjec : gameObjecs) {
			gameObjec.init();
		}
				
		System.out.println("Initialization Done!\n");
	}
	
	@Override
	public void input() {
		
		if(Keyboard.activeKeyEvent()) {
			if(Keyboard.isKeyDown(GLFW_KEY_ESCAPE)) {
				end();
			}
		}
		
		for(GameObject gameObjec : gameObjecs) {
			gameObjec.input();
		}
	}

	@Override
	public void update(float delta) {
		for(GameObject gameObjec : gameObjecs) {
			gameObjec.update(delta);
		}
	}

	@Override
	public void render() {
	
		glClearColor(0.9f, 0.9f, 0.9f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		for(GameObject gameObjec : gameObjecs) {
			gameObjec.render();
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
