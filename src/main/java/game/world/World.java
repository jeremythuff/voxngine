package game.world;

import java.util.ArrayList;
import java.util.List;

import game.GameObject;
import game.world.terrain.Zone;
import voxngine.graphics.RenderEngine;
import voxngine.io.Controlls;
import voxngine.io.ScreenMessage;
import voxngine.io.Window;

public class World implements GameObject {
		
	List<WorldObject> worldObjects = new ArrayList<WorldObject>();

	public void init(RenderEngine renderer) {
		
		worldObjects.add(new Zone());
		
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
			try {
				worldObject.update(delta);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
