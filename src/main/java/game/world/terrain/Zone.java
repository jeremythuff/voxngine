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
		
		worldObjects.add(new Chunk(20, 10, 20, 50, 10, -30));
		worldObjects.add(new Chunk(20, 10, 20, 50, 10, -10));
		worldObjects.add(new Chunk(20, 10, 20, 50, 10, 10));
		worldObjects.add(new Chunk(20, 10, 20, 50, 10, 30));
		worldObjects.add(new Chunk(20, 10, 20, 50, 10, 50));
		
		worldObjects.add(new Chunk(20, 10, 20, 30, 10, -30));
		worldObjects.add(new Chunk(20, 10, 20, 30, 10, -10));
		worldObjects.add(new Chunk(20, 10, 20, 30, 10, 10));
		worldObjects.add(new Chunk(20, 10, 20, 30, 10, 30));
		worldObjects.add(new Chunk(20, 10, 20, 30, 10, 50));

		
		worldObjects.add(new Chunk(20, 10, 20, 10, 10, -30));
		worldObjects.add(new Chunk(20, 10, 20, 10, 10, -10));
		worldObjects.add(new Chunk(20, 10, 20, 10, 10, 10)); // center
		worldObjects.add(new Chunk(20, 10, 20, 10, 10, 30));
		worldObjects.add(new Chunk(20, 10, 20, 10, 10, 50));
		
		worldObjects.add(new Chunk(20, 10, 20, -10, 10, 50));
		worldObjects.add(new Chunk(20, 10, 20, -10, 10, 30));
		worldObjects.add(new Chunk(20, 10, 20, -10, 10, 10));
		worldObjects.add(new Chunk(20, 10, 20, -10, 10, -10));
		worldObjects.add(new Chunk(20, 10, 20, -10, 10, -30));
		
		worldObjects.add(new Chunk(20, 10, 20, -30, 10, 50));
		worldObjects.add(new Chunk(20, 10, 20, -30, 10, 30));
		worldObjects.add(new Chunk(20, 10, 20, -30, 10, 10));
		worldObjects.add(new Chunk(20, 10, 20, -30, 10, -10));
		worldObjects.add(new Chunk(20, 10, 20, -30, 10, -30));
		
		//
		
		worldObjects.add(new Chunk(20, 10, 20, 50, 20, -30));
		worldObjects.add(new Chunk(20, 10, 20, 50, 20, -10));
		worldObjects.add(new Chunk(20, 10, 20, 50, 20, 10));
		worldObjects.add(new Chunk(20, 10, 20, 50, 20, 30));
		worldObjects.add(new Chunk(20, 10, 20, 50, 20, 50));
		
		worldObjects.add(new Chunk(20, 10, 20, 30, 20, -30));
		worldObjects.add(new Chunk(20, 10, 20, 30, 20, -10));
		worldObjects.add(new Chunk(20, 10, 20, 30, 20, 10));
		worldObjects.add(new Chunk(20, 10, 20, 30, 20, 30));
		worldObjects.add(new Chunk(20, 10, 20, 30, 20, 50));

		
		worldObjects.add(new Chunk(20, 10, 20, 10, 20, -30));
		worldObjects.add(new Chunk(20, 10, 20, 10, 20, -10));
		worldObjects.add(new Chunk(20, 10, 20, 10, 20, 10)); // center
		worldObjects.add(new Chunk(20, 10, 20, 10, 20, 30));
		worldObjects.add(new Chunk(20, 10, 20, 10, 20, 50));
		
		worldObjects.add(new Chunk(20, 10, 20, -10, 20, 50));
		worldObjects.add(new Chunk(20, 10, 20, -10, 20, 30));
		worldObjects.add(new Chunk(20, 10, 20, -10, 20, 10));
		worldObjects.add(new Chunk(20, 10, 20, -10, 20, -10));
		worldObjects.add(new Chunk(20, 10, 20, -10, 20, -30));
		
		worldObjects.add(new Chunk(20, 10, 20, -30, 20, 50));
		worldObjects.add(new Chunk(20, 10, 20, -30, 20, 30));
		worldObjects.add(new Chunk(20, 10, 20, -30, 20, 10));
		worldObjects.add(new Chunk(20, 10, 20, -30, 20, -10));
		worldObjects.add(new Chunk(20, 10, 20, -30, 20, -30));
		
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
