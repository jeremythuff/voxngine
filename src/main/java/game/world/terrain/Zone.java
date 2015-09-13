package game.world.terrain;

import java.util.ArrayList;
import java.util.List;

import game.world.WorldObject;
import voxngine.graphics.RenderEngine;
import voxngine.io.Controlls;
import voxngine.io.ScreenMessage;
import voxngine.io.Window;

public class Zone implements WorldObject {
	
	List<WorldObject> worldObjects = new ArrayList<WorldObject>();
	int i;

	@Override
	public void init(RenderEngine renderer) {
	
		worldObjects.add(new Chunk(50, 20, 50, 125, 10, -75));
		worldObjects.add(new Chunk(50, 20, 50, 125, 10, -25));
		worldObjects.add(new Chunk(50, 20, 50, 125, 10, 25));
		worldObjects.add(new Chunk(50, 20, 50, 125, 10, 75));
		worldObjects.add(new Chunk(50, 20, 50, 125, 10, 125));
		
		worldObjects.add(new Chunk(50, 20, 50, 75, 10, -75));
		worldObjects.add(new Chunk(50, 20, 50, 75, 10, -25));
		worldObjects.add(new Chunk(50, 20, 50, 75, 10, 25));
		worldObjects.add(new Chunk(50, 20, 50, 75, 10, 75));
		worldObjects.add(new Chunk(50, 20, 50, 75, 10, 125));

		
		worldObjects.add(new Chunk(50, 20, 50, 25, 10, -75));
		worldObjects.add(new Chunk(50, 20, 50, 25, 10, -25));
		worldObjects.add(new Chunk(50, 20, 50, 25, 10, 25)); // center
		worldObjects.add(new Chunk(50, 20, 50, 25, 10, 75));
		worldObjects.add(new Chunk(50, 20, 50, 25, 10, 125));
		
		worldObjects.add(new Chunk(50, 20, 50, -25, 10, 125));
		worldObjects.add(new Chunk(50, 20, 50, -25, 10, 75));
		worldObjects.add(new Chunk(50, 20, 50, -25, 10, 25));
		worldObjects.add(new Chunk(50, 20, 50, -25, 10, -25));
		worldObjects.add(new Chunk(50, 20, 50, -25, 10, -75));
		
		worldObjects.add(new Chunk(50, 20, 50, -75, 10, 125));
		worldObjects.add(new Chunk(50, 20, 50, -75, 10, 75));
		worldObjects.add(new Chunk(50, 20, 50, -75, 10, 25));
		worldObjects.add(new Chunk(50, 20, 50, -75, 10, -25));
		worldObjects.add(new Chunk(50, 20, 50, -75, 10, -75));
		
		worldObjects.stream().forEach(worldObject -> {		
			System.out.println("Initialiazing the "+ worldObject.getClass().getSimpleName() +" Object...");
			worldObject.init(renderer);
		});
		
	}
	
	@Override
	public void input(Controlls controlls) {
		worldObjects.stream().forEach(worldObject -> {
			worldObject.input(controlls);
		});
	}

	@Override
	public void update(float delta) {
		worldObjects.stream().forEach(worldObject -> {
			worldObject.update(delta);
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
