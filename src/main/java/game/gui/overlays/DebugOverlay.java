package game.gui.overlays;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_P;

import org.joml.Vector3f;

import game.gui.GuiObject;
import voxngine.graphics.RenderEngine;
import voxngine.graphics.textures.Font;
import voxngine.io.Controlls;
import voxngine.io.ScreenMessage;
import voxngine.io.Window;


public class DebugOverlay implements GuiObject {
				
	private float GENERAL_lineStart = 50f;
	private float RENDERED_OBJECT_lineStart = 50f;
	private float CAMERA_DATA_spacingStart = 100f;
	
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
		
		float GENERAL_lineIncr = GENERAL_lineStart;
		float RENDERED_OBJECT_lineIncr = RENDERED_OBJECT_lineStart;
		float CAMERA_DATA_spacing = CAMERA_DATA_spacingStart;
		
		if(Window.getScreenMessages("DebugOverlay") != null) {
			for(int i = 0 ; i < Window.getScreenMessages("DebugOverlay").size() ; i++) {
				ScreenMessage screenMessage = Window.popMessage("DebugOverlay");
				
				if(show_tex)
					if(screenMessage.getType().equals("GENERAL")) {
						renderer.print(50f, GENERAL_lineIncr, debugFont, screenMessage.getMessage());
						GENERAL_lineIncr += 20;
					} else if(screenMessage.getType().equals("TITLE")) {
						float center = (Window.WIDTH/2)-((screenMessage.getMessage().length()*12)/2);
						renderer.print(center, 50f, debugFont, screenMessage.getMessage(),new Vector3f(0.2f, 0.2f, 0.8f));
					} else if(screenMessage.getType().equals("CAMERA_DATA")) {
						renderer.print(CAMERA_DATA_spacing, Window.HEIGHT - 50, debugFont, screenMessage.getMessage(), new Vector3f(0.8f, 0.8f, 0.8f));
						
						CAMERA_DATA_spacing += 150;
						
					}if(screenMessage.getType().equals("RENDERED_OBJECT")) {
												
						float position = Window.WIDTH-screenMessage.getMessage().length()*12-50;
						renderer.print(position, RENDERED_OBJECT_lineIncr, debugFont, screenMessage.getMessage(), new Vector3f(0.8f, 0.8f, 0.8f));
						RENDERED_OBJECT_lineIncr += 20;
					}
				
			}
		}
		
	}

	@Override
	public void dispose() {}

	
	
	

}
