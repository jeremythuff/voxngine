package game.world.terrain;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_RIGHT;

import java.util.ArrayList;
import java.util.List;

import game.world.WorldObject;
import voxngine.graphics.RenderEngine;
import voxngine.io.Controlls;
import voxngine.io.ScreenMessage;
import voxngine.io.Window;

public class Zone implements WorldObject {
	
	List<WorldObject> worldObjects = new ArrayList<WorldObject>();
	
	int activeChunk;
	int lastActive;
	
	int i;
	int u;

	private boolean setLastActive;

	@Override
	public void init(RenderEngine renderer) {
		
//		worldObjects.add(new Chunk(50, 20, 50, 125, 10, -75));
//		worldObjects.add(new Chunk(50, 20, 50, 125, 10, -25));
//		worldObjects.add(new Chunk(50, 20, 50, 125, 10, 25));
//		worldObjects.add(new Chunk(50, 20, 50, 125, 10, 75));
//		worldObjects.add(new Chunk(50, 20, 50, 125, 10, 125));
//		
//		worldObjects.add(new Chunk(50, 20, 50, 75, 10, -75));
//		worldObjects.add(new Chunk(50, 20, 50, 75, 10, -25));
//		worldObjects.add(new Chunk(50, 20, 50, 75, 10, 25));
//		worldObjects.add(new Chunk(50, 20, 50, 75, 10, 75));
//		worldObjects.add(new Chunk(50, 20, 50, 75, 10, 125));
//
//		
//		worldObjects.add(new Chunk(50, 20, 50, 25, 10, -75));
		//worldObjects.add(new Chunk(50, 20, 50, 25, 10, -25));
		worldObjects.add(new Chunk(250, 40, 250, 0, 0, 0)); // center
		//worldObjects.add(new Chunk(50, 20, 50, 25, 10, 75));
//		worldObjects.add(new Chunk(50, 20, 50, 25, 10, 125));
//		
//		worldObjects.add(new Chunk(50, 20, 50, -25, 10, 125));
//		worldObjects.add(new Chunk(50, 20, 50, -25, 10, 75));
//		worldObjects.add(new Chunk(50, 20, 50, -25, 10, 25));
//		worldObjects.add(new Chunk(50, 20, 50, -25, 10, -25));
//		worldObjects.add(new Chunk(50, 20, 50, -25, 10, -75));
//		
//		worldObjects.add(new Chunk(50, 20, 50, -75, 10, 125));
//		worldObjects.add(new Chunk(50, 20, 50, -75, 10, 75));
//		worldObjects.add(new Chunk(50, 20, 50, -75, 10, 25));
//		worldObjects.add(new Chunk(50, 20, 50, -75, 10, -25));
//		worldObjects.add(new Chunk(50, 20, 50, -75, 10, -75));
		
		activeChunk = worldObjects.size() - 1;
		
		worldObjects.stream().forEach(worldObject -> {		
			System.out.println("Initialiazing the "+ worldObject.getClass().getSimpleName() +" Object...");
			worldObject.init(renderer);
		});
		
	}
	
	@Override
	public void input(Controlls controlls) {
		
		if(controlls.getKeyboad().activeKeyEvent()) {
			if(controlls.getKeyboad().isKeyDown(GLFW_KEY_LEFT)) {
				setLastActive = true;
				lastActive = activeChunk;
				activeChunk++;
				if(activeChunk > worldObjects.size() - 1) {
					lastActive = worldObjects.size() - 1;
					activeChunk = 0;
				}
			}
			
			if(controlls.getKeyboad().isKeyDown(GLFW_KEY_RIGHT)) {
				setLastActive = true;
				lastActive = activeChunk;
				activeChunk--;
				if(activeChunk < 0) {
					lastActive = 0;
					activeChunk = worldObjects.size() - 1; 
				}
			}
			
		}
		
		Window.queScreenMessage("DebugOverlay", new ScreenMessage("Last Active: "+lastActive));
		Window.queScreenMessage("DebugOverlay", new ScreenMessage("Active: "+activeChunk));
		
		worldObjects.stream().forEach(worldObject -> {
			worldObject.input(controlls);
		});
	}

	@Override
	public void update(float delta) {
		u = 0;
		worldObjects.stream().forEach(worldObject -> {
			
			if(u == lastActive && setLastActive) {
				setLastActive = false;
				((Chunk)worldObject).setLastActive(true);
				((Chunk)worldObject).setActive(false);
			} else if(u == activeChunk)  {
				((Chunk)worldObject).setActive(true);
				((Chunk)worldObject).setLastActive(false);
			} else {
				((Chunk)worldObject).setActive(false);
				((Chunk)worldObject).setLastActive(false);
			}
			
			worldObject.update(delta);
			u++;
		});
	}

	@Override
	public void render(RenderEngine renderer) {
		
		i = 0;
		worldObjects.stream().forEach(worldObject -> {
			i++;
			worldObject.render(renderer);
		});
		
		Window.queScreenMessage("DebugOverlay", new ScreenMessage("Chunks: "+ i));			
		
	}

	public void dispose() {
		worldObjects.stream().forEach(worldObject -> {
			worldObject.dispose();
		});
	}

}
