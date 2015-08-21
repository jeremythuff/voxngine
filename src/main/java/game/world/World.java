package game.world;

import game.world.World;
import game.world.terrain.Voxel;

import java.util.ArrayList;
import java.util.List;

public class World {
		
	List<WorldObject> worldObjects = new ArrayList<WorldObject>();

	public void init() {		
		
		worldObjects.add(new Voxel());
	
		for(WorldObject worldObject : worldObjects) {			
			System.out.println("Initialiazing the "+ worldObject.getClass().getSimpleName() +" Object...");
			worldObject.init();
		}
		
	}

	public void update(float delta) {
		for(WorldObject worldObject : worldObjects) {
			worldObject.update(delta);
		}
		
	}

	public void render(float delta) {
		for(WorldObject worldObject : worldObjects) {
			worldObject.render(delta);
		}
	}

	public void dispose() {
		for(WorldObject worldObject : worldObjects) {
			System.out.println("Disposing of the "+ worldObject.getClass().getSimpleName() +" Object...");
			worldObject.dispose();
		}
	}
	
}
