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
	
		for(GuiObject guiObject : guiObjects) {			
			System.out.println("Initialiazing the "+ guiObject.getClass().getSimpleName() +" Object...");
			guiObject.init(renderer);
		}
		
	}

	public void update(float delta) {
		for(GuiObject guiObject : guiObjects) {
			guiObject.update(delta);
		}
		
	}

	public void render(RenderEngine renderer) {
		for(GuiObject guiObject : guiObjects) {
			guiObject.render(renderer);
		}
	}

	public void dispose() {
		for(GuiObject guiObject : guiObjects) {
			System.out.println("Disposing of the "+ guiObject.getClass().getSimpleName() +" Object...");
			guiObject.dispose();
		}
	}

	@Override
	public void input(Controlls controlls) {
		// TODO Auto-generated method stub
		
	}
	
}
