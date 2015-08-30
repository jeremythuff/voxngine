package game.world;

import game.GameObject;
import game.world.World;
import game.world.terrain.Cube;
import game.world.terrain.Zone;
import voxngine.graphics.RenderEngine;

import java.util.ArrayList;
import java.util.List;

public class World implements GameObject {
		
	List<WorldObject> worldObjects = new ArrayList<WorldObject>();

	public void init() {
		
		worldObjects.add(new Zone());
		worldObjects.add(new Cube());
	
		for(WorldObject worldObject : worldObjects) {			
			System.out.println("Initialiazing the "+ worldObject.getClass().getSimpleName() +" Object...");
			worldObject.init();
		}
		
	}
	
	@Override
	public void input() {
		for(WorldObject worldObject : worldObjects) {
			worldObject.input();
		}
		RenderEngine.initVbos();
	}

	@Override
	public void update(float delta) {
		for(WorldObject worldObject : worldObjects) {
			worldObject.update(delta);
		}
	}

	@Override
	public void render() {
		for(WorldObject worldObject : worldObjects) {
			worldObject.render();
		}
	}

	public void dispose() {
		for(WorldObject worldObject : worldObjects) {
			System.out.println("Disposing of the "+ worldObject.getClass().getSimpleName() +" Object...");
			worldObject.dispose();
		}
	}

	
}
