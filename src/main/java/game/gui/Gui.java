package game.gui;

import java.util.ArrayList;
import java.util.List;

import game.GameObject;
import game.gui.overlays.DebugOverlay;
import voxngine.graphics.RenderEngine;
import voxngine.io.Controlls;

public class Gui implements GameObject {
		
	List<GuiObject> guiObjects = new ArrayList<GuiObject>();

	public void init(RenderEngine renderer) {		
		
		guiObjects.add(new DebugOverlay());
	
		guiObjects.stream().forEach(guiObject -> {			
			System.out.println("Initialiazing the "+ guiObject.getClass().getSimpleName() +" Object...");
			guiObject.init(renderer);
		});
		
		renderer.initText();
		
	}

	public void update(float delta) {
		guiObjects.stream().forEach(guiObject -> {
			guiObject.update(delta);
		});
		
	}

	public void render(RenderEngine renderer) {
		guiObjects.stream().forEach(guiObject -> {
			guiObject.render(renderer);
		});
	}

	public void dispose() {
		guiObjects.stream().forEach(guiObject -> {
			System.out.println("Disposing of the "+ guiObject.getClass().getSimpleName() +" Object...");
			guiObject.dispose();
		});
	}

	@Override
	public void input(Controlls controlls) {
		guiObjects.stream().forEach(guiObject -> {
			guiObject.input(controlls);
		});
		
	}
	
}
