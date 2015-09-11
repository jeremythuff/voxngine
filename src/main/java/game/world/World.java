package game.world;

import game.GameObject;
import game.world.World;
import game.world.terrain.Cube;
import game.world.terrain.Zone;
import voxngine.graphics.RenderEngine;
import voxngine.io.Controlls;
import voxngine.io.ScreenMessage;
import voxngine.io.Window;

import java.util.ArrayList;
import java.util.List;

public class World implements GameObject {
		
	List<WorldObject> worldObjects = new ArrayList<WorldObject>();

	public void init(RenderEngine renderer) {
		
		worldObjects.add(new Zone());
//		worldObjects.add(new Cube(16, 16, 16, 0, 0, 0));
//		worldObjects.add(new Cube(16, 16, 16, 16, 0, 0));
//		worldObjects.add(new Cube(16, 16, 16, 0, 0, 16));
//		worldObjects.add(new Cube(16, 16, 16, 16, 0, 16));
//		
//		worldObjects.add(new Cube(16, 16, 16, 0, 16, 0));
//		worldObjects.add(new Cube(16, 16, 16, 16, 16, 0));
//		worldObjects.add(new Cube(16, 16, 16, 0, 16, 16));
//		worldObjects.add(new Cube(16, 16, 16, 16, 16, 16));
//		
//		worldObjects.add(new Cube(16, 16, 16, 0, -16, 0));
//		worldObjects.add(new Cube(16, 16, 16, 16, -16, 0));
//		worldObjects.add(new Cube(16, 16, 16, 0, -16, 16));
//		worldObjects.add(new Cube(16, 16, 16, 16, -16, 16));
		worldObjects.add(new Cube(100, 100, 100, 100, 0, 0));
		worldObjects.add(new Cube(100, 100, 100, 0, 0, 100));
		worldObjects.add(new Cube(100, 100, 100, 0, 0, 0));
	
		renderer.initShaders();
		
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
		worldObjects.stream().forEach(worldObject -> {
			worldObject.render(renderer);
			Window.queScreenMessage("DebugOverlay", new ScreenMessage("RENDERED_OBJECT", worldObject.getClass().getSimpleName()));			
		});
	}

	public void dispose() {
		worldObjects.stream().forEach(worldObject -> {
			System.out.println("Disposing of the "+ worldObject.getClass().getSimpleName() +" Object...");
			worldObject.dispose();
		});
	}

	
}
