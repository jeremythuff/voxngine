package game;

import voxngine.Engine;
import voxngine.io.Window;

import static org.lwjgl.glfw.GLFW.glfwSetWindowTitle;

import java.util.ArrayList;
import java.util.List;

import game.gui.Gui;
import game.world.World;

public class Game extends Engine {
	
	List<GameObject> gameObjecs = new ArrayList<GameObject>();
			
	public static void main(String[] args) {
        new Game().start();
    }

	@Override
	public void init() {
		
		glfwSetWindowTitle(Window.id, "Voxel Game");
		
		System.out.println("Game Started!");
		
		gameObjecs.add(new World());
		gameObjecs.add(new Gui());
		
		for(GameObject gameObjec : gameObjecs) {
			gameObjec.init();
		}
		
		System.out.println("Initialization Done!");
	}

	@Override
	public void update(float delta) {
		for(GameObject gameObjec : gameObjecs) {
			gameObjec.update(delta);
		}
	}

	@Override
	public void render(float delta) {
		for(GameObject gameObjec : gameObjecs) {
			gameObjec.render(delta);
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
