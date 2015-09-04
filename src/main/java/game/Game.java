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
		
		
		gameObjecs.stream().forEach(gameObject -> {
			gameObject.init(renderer);
		});
		
		System.out.println("Initialization Done!\n");
	}
	
	@Override
	public void input(Controlls controlls) {
		
		if(controlls.getKeyboad().activeKeyEvent()) {
			if(controlls.getKeyboad().isKeyDown(GLFW_KEY_ESCAPE)) {
				end();
			}
		}
		
		gameObjecs.parallelStream().forEach(gameObject -> {
			gameObject.input(controlls);
		});
	}

	@Override
	public void update(float delta) {
		gameObjecs.parallelStream().forEach(gameObject -> {
			gameObject.update(delta);
		});
	}

	@Override
	public void render(RenderEngine renderer) {
	
		glClearColor(0.9f, 0.9f, 0.9f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		gameObjecs.parallelStream().forEach(gameObject -> {
			gameObject.render(renderer);
		});
	}
	
	@Override
	public void dispose() {
		System.out.println("Shutting down...");
		gameObjecs.parallelStream().forEach(gameObject -> {
			gameObject.dispose();
		});
		System.out.println("Game Ended!");
	}
	
}
