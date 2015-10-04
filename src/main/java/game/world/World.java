package game.world;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_1;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_2;

import java.util.ArrayList;
import java.util.List;

import game.GameObject;
import game.world.terrain.Zone;
import voxngine.graphics.RenderEngine;
import voxngine.io.Controlls;
import voxngine.io.ScreenMessage;
import voxngine.io.Window;

public class World implements GameObject {
	
	private RenderEngine renderer;
	
	private boolean displayIntro;
		
	private WorldGenerator worldGenerator;
	private List<WorldObject> worldObjects = new ArrayList<WorldObject>();

	public void init(RenderEngine renderer) {
		
		displayIntro = true;
		
		worldGenerator = new WorldGenerator();
		
		this.renderer = renderer; 
		
		renderer.initShaders();
		
	}
	
	@Override
	public void input(Controlls controlls) {
		
		if(controlls.getKeyboad().activeKeyEvent()) {
			if(controlls.getKeyboad().isKeyDown(GLFW_KEY_1)) {
				worldGenerator.makeWorld(60);
			}
			
			if(controlls.getKeyboad().isKeyDown(GLFW_KEY_2)) {
				worldObjects.add(new Zone());
				
				worldObjects.stream().forEach(worldObject -> {		
					System.out.println("Initialiazing the "+ worldObject.getClass().getSimpleName() +" Object...");
					worldObject.init(renderer);
				});
			}
		}
		
//		if(displayIntro) {
//			Window.queScreenMessage("StartMenu", new ScreenMessage("Last Active: "+lastActive));
//		}
		
		
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
