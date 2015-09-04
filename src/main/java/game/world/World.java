package game.world;

import game.GameObject;
import game.world.World;
import game.world.terrain.Cube;
import game.world.terrain.Zone;
import voxngine.graphics.RenderEngine;
import voxngine.io.Controlls;

import java.util.ArrayList;
import java.util.List;

public class World implements GameObject {
		
	List<WorldObject> worldObjects = new ArrayList<WorldObject>();

	public void init(RenderEngine renderer) {
		
		worldObjects.add(new Zone());
		worldObjects.add(new Cube(100, 8, 100, 50, 4, 50));
	
		for(WorldObject worldObject : worldObjects) {			
			System.out.println("Initialiazing the "+ worldObject.getClass().getSimpleName() +" Object...");
			worldObject.init(renderer);
		}
		
		
		renderer.initVbos();
	}
	
	@Override
	public void input(Controlls controlls) {
		for(WorldObject worldObject : worldObjects) {
			worldObject.input(controlls);
		}
	}

	@Override
	public void update(float delta) {
		for(WorldObject worldObject : worldObjects) {
			worldObject.update(delta);
		}
	}

	@Override
	public void render(RenderEngine renderer) {
		for(WorldObject worldObject : worldObjects) {
			worldObject.render(renderer);
		}
	}

	public void dispose() {
		for(WorldObject worldObject : worldObjects) {
			System.out.println("Disposing of the "+ worldObject.getClass().getSimpleName() +" Object...");
			worldObject.dispose();
		}
	}

	
}
