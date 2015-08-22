package game.gui;

import java.util.ArrayList;
import java.util.List;

import game.GameObject;
import game.gui.overlays.DebugOverlay;

public class Gui implements GameObject {
		
	List<GuiObject> guiObjects = new ArrayList<GuiObject>();

	public void init() {		
		
		guiObjects.add(new DebugOverlay());
	
		for(GuiObject guiObject : guiObjects) {			
			System.out.println("Initialiazing the "+ guiObject.getClass().getSimpleName() +" Object...");
			guiObject.init();
		}
		
	}

	public void update(float delta) {
		for(GuiObject guiObject : guiObjects) {
			guiObject.update(delta);
		}
		
	}

	public void render(float delta) {
		for(GuiObject guiObject : guiObjects) {
			guiObject.render(delta);
		}
	}

	public void dispose() {
		for(GuiObject guiObject : guiObjects) {
			System.out.println("Disposing of the "+ guiObject.getClass().getSimpleName() +" Object...");
			guiObject.dispose();
		}
	}
	
}
