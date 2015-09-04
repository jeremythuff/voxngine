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
		worldObjects.add(new Cube(200, 10, 200, 12, 50, 12));
		worldObjects.add(new Cube(25, 5, 25, 12, 12, 12));
		worldObjects.add(new Cube(1, 1, 1, 0, 0, 0));
		worldObjects.add(new Cube(1, 2, 1, 5, -5, 5));
		worldObjects.add(new Cube(3, 2, 1, 15, -5, 5));
	
		worldObjects.parallelStream().forEach(worldObject -> {		
			System.out.println("Initialiazing the "+ worldObject.getClass().getSimpleName() +" Object...");
			worldObject.init(renderer);
		});
		renderer.initVbos();
	}
	
	@Override
	public void input(Controlls controlls) {
		worldObjects.parallelStream().forEach(worldObject -> {
			worldObject.input(controlls);
		});
	}

	@Override
	public void update(float delta) {
		worldObjects.parallelStream().forEach(worldObject -> {
			worldObject.update(delta);
		});
	}

	@Override
	public void render(RenderEngine renderer) {
		worldObjects.parallelStream().forEach(worldObject -> {
			worldObject.render(renderer);
		});
	}

	public void dispose() {
		worldObjects.parallelStream().forEach(worldObject -> {
			System.out.println("Disposing of the "+ worldObject.getClass().getSimpleName() +" Object...");
			worldObject.dispose();
		});
	}

	
}
