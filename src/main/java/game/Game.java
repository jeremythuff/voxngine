package game;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
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
import voxngine.io.ScreenMessage;
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
		
		Window.setName("Voxel Game");
		
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
		
		Window.queScreenMessage("DebugOverlay", new ScreenMessage("Mouse x: "+(int)controlls.getMouse().getPos().x));
		Window.queScreenMessage("DebugOverlay", new ScreenMessage("Mouse Y: "+(int)controlls.getMouse().getPos().y));
		
		if(controlls.getKeyboad().activeKeyEvent()) {
			if(controlls.getKeyboad().isKeyDown(GLFW_KEY_ESCAPE)) {
				end();
			}
		}
		
		gameObjecs.stream().forEach(gameObject -> {
			gameObject.input(controlls);
		});
	}

	@Override
	public void update(float delta) {
		gameObjecs.stream().forEach(gameObject -> {
			gameObject.update(delta);
		});
	}

	@Override
	public void render(RenderEngine renderer) {
	
		Window.queScreenMessage("DebugOverlay", new ScreenMessage("TITLE", "DEBUG: "+ Window.getName()));
		
		glClearColor(0.5f, 0.5f, 0.5f, 1.0f);
		glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
		
		gameObjecs.stream().forEach(gameObject -> {
			gameObject.render(renderer);
		});
		
	}
	
	@Override
	public void dispose() {
		System.out.println("Shutting down...");
		gameObjecs.stream().forEach(gameObject -> {
			gameObject.dispose();
		});
		System.out.println("Game Ended!");
	}
	
}
