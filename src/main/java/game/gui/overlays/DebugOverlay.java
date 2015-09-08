package game.gui.overlays;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_P;

import game.gui.GuiObject;
import voxngine.graphics.RenderEngine;
import voxngine.graphics.textures.Font;
import voxngine.io.Controlls;
import voxngine.io.ScreenMessage;
import voxngine.io.Window;


public class DebugOverlay implements GuiObject {
				
	private float lineStart = 50f;
	
	private boolean show_tex = true;
	
	private Font debugFont;

	@Override
	public void init(RenderEngine renderer) {
		
        debugFont = new Font("fonts/Cabin-Regular.ttf");
		
	}
	
	@Override
	public void input(Controlls controlls) {
		
		if(controlls.getKeyboad().activeKeyEvent()) {
			if(controlls.getKeyboad().isKeyDown(GLFW_KEY_LEFT_CONTROL) &&controlls.getKeyboad().isKeyDown(GLFW_KEY_P)) {
				if(show_tex) {
					show_tex = false;
				} else {
					show_tex = true;
				}			
			}
		}
		
	}

	@Override
	public void update(float delta) {}

	@Override
	public void render(RenderEngine renderer) {
		
		float lineIncr = lineStart;
		
		
		if(Window.getScreenMessages("DebugOverlay") != null) {
			for(int i = 0 ; i < Window.getScreenMessages("DebugOverlay").size() ; i++) {
				ScreenMessage screenMessage = Window.popMessage("DebugOverlay");
				
				if(show_tex)
					if(screenMessage.getType().equals("GENERAL")) {
						renderer.print(50f, lineIncr, debugFont, screenMessage.getMessage());
						lineIncr += 20;
					} else if(screenMessage.getType().equals("TITLE")) {
						float center = (Window.WIDTH/2)-((screenMessage.getMessage().length()*12)/2);
						renderer.print(center, 50f, debugFont, screenMessage.getMessage());
					}
				
			}
		}
		
	}

	@Override
	public void dispose() {}

	
	
	

}
