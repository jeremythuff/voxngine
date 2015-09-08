package game.gui.overlays;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_P;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_RED;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.stb.STBTruetype.stbtt_GetPackedQuad;
import static org.lwjgl.stb.STBTruetype.stbtt_PackBegin;
import static org.lwjgl.stb.STBTruetype.stbtt_PackEnd;
import static org.lwjgl.stb.STBTruetype.stbtt_PackFontRange;
import static org.lwjgl.stb.STBTruetype.stbtt_PackSetOversampling;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;

import game.gui.GuiObject;
import voxngine.graphics.RenderEngine;
import voxngine.graphics.Vao;
import voxngine.graphics.Vbo;
import voxngine.graphics.shaders.Shader;
import voxngine.graphics.shaders.ShaderProgram;
import voxngine.graphics.textures.Font;
import voxngine.graphics.textures.Texture;
import voxngine.io.Controlls;
import voxngine.io.ScreenMessage;
import voxngine.io.Window;
import voxngine.utils.FileUtils;


public class DebugOverlay implements GuiObject {
	
private ShaderProgram shaderProgram;
			
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
		
		
		Window.queScreenMessage("DebugOverlay", new ScreenMessage("Mouse x: "+(int)controlls.getMouse().getPos().x));
		Window.queScreenMessage("DebugOverlay", new ScreenMessage("Mouse Y: "+(int)controlls.getMouse().getPos().y));
		
		
	}

	@Override
	public void update(float delta) {
		
		
	}

	@Override
	public void render(RenderEngine renderer) {
		
		float lineIncr = lineStart;
		
		if(Window.getScreenMessages("DebugOverlay") != null) {
			for(int i = 0 ; i < Window.getScreenMessages("DebugOverlay").size() ; i++) {
				ScreenMessage screenMessage = Window.popMessage("DebugOverlay");
				
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
	public void dispose() {
		
		
	}

	
	
	

}
