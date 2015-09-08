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
	
	int count;
    private int numVertices;
	
    private Vao textVao;
    private Vbo textVbo;
    
    private FloatBuffer vertices;
		
	private float lineStart = 50f;
	
	private boolean show_tex = true;
	
	private Font debugFont;
	
	private final STBTTAlignedQuad q  = new STBTTAlignedQuad();
	private final FloatBuffer      xb = BufferUtils.createFloatBuffer(1);
	private final FloatBuffer      yb = BufferUtils.createFloatBuffer(1);

	@Override
	public void init(RenderEngine renderer) {
		
		textVao = new Vao();
		
		textVbo = new Vbo();
		
		 /* Create FloatBuffer */
        vertices = BufferUtils.createFloatBuffer(4096);

        textVao.bind();
        
        /* Upload null data to allocate storage for the VBO */
        long size = vertices.capacity() * Float.BYTES;
        textVbo.bind(GL_ARRAY_BUFFER);
        textVbo.uploadData(GL_ARRAY_BUFFER, size, GL_DYNAMIC_DRAW);
        
        /* Initialize variables */
        numVertices = 0;
        
        shaderProgram = new ShaderProgram();
        
		Shader vShad = shaderProgram.loadShader(GL_VERTEX_SHADER, "src/main/resources/shaders/text.vs");
		shaderProgram.attachShader(vShad);
		vShad.delete();
		
		Shader fShad = shaderProgram.loadShader(GL_FRAGMENT_SHADER, "src/main/resources/shaders/text.fs");
		shaderProgram.attachShader(fShad);
		fShad.delete();
				
		shaderProgram.link();
		shaderProgram.bind();
		
		 /* Specify Vertex Pointer */
        int posAttrib = shaderProgram.getAttributeLocation("fontPos");
        shaderProgram.enableVertexAttribute(posAttrib);
        shaderProgram.pointVertexAttribute(posAttrib, 2, 7 * Float.BYTES, 0);

        /* Specify Color Pointer */
        int colAttrib = shaderProgram.getAttributeLocation("fontColor");
        shaderProgram.enableVertexAttribute(colAttrib);
        shaderProgram.pointVertexAttribute(colAttrib, 3, 7 * Float.BYTES, 2 * Float.BYTES);

        /* Specify Texture Pointer */
        int texAttrib = shaderProgram.getAttributeLocation("fontTexcoord");
        shaderProgram.enableVertexAttribute(texAttrib);
        shaderProgram.pointVertexAttribute(texAttrib, 2, 7 * Float.BYTES, 5 * Float.BYTES);

        /* Set texture uniform */
        int uniTex = shaderProgram.getUniformLocation("texImage");
        shaderProgram.setUniform(uniTex, 0);

        /* Set model matrix to identity matrix */
        Matrix4f model = new Matrix4f();
        int uniModel = shaderProgram.getUniformLocation("model");
        shaderProgram.setUniform(uniModel, model);

        /* Set view matrix to identity matrix */
        Matrix4f view = new Matrix4f();
        int uniView = shaderProgram.getUniformLocation("view");
        shaderProgram.setUniform(uniView, view);

        /* Set projection matrix to an orthographic projection */
        Matrix4f projection = new Matrix4f();
        int uniProjection = shaderProgram.getUniformLocation("projection");
        shaderProgram.setUniform(uniProjection, projection.ortho(0.0f, Window.WIDTH, Window.HEIGHT, 0.0f, -1.0f, 1.0f));

        /* Enable blending */
        
        debugFont = new Font("fonts/Cabin-Regular.ttf");
		
		count = 0;
		
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
		
		float lineIncr = lineStart;
		
		if(Window.getScreenMessages("DebugOverlay") != null) {
			for(int i = 0 ; i < Window.getScreenMessages("DebugOverlay").size() ; i++) {
				ScreenMessage screenMessage = Window.popMessage("DebugOverlay");
				
				if(screenMessage.getType().equals("GENERAL")) {
					print(50f, lineIncr, debugFont.getFontTexture().getId(), screenMessage.getMessage());
					lineIncr += 20;
				} else if(screenMessage.getType().equals("TITLE")) {
					float center = (Window.WIDTH/2)-((screenMessage.getMessage().length()*12)/2);
					print(center, 50f, debugFont.getFontTexture().getId(), screenMessage.getMessage());
				}
				
			}
		}
	}

	@Override
	public void render(RenderEngine renderer) {
		if (numVertices > 0 && show_tex) {
            vertices.flip();
            textVao.bind();
            shaderProgram.bind();
            /* Upload the new vertex data */
            textVbo.bind(GL_ARRAY_BUFFER);
            textVbo.uploadSubData(GL_ARRAY_BUFFER, 0, vertices);
            /* Draw batch */
            glDrawArrays(GL_TRIANGLES, 0, numVertices);
            /* Clear vertex data for next batch */
            numVertices = 0;
        }
		
		vertices.clear();
		
	}

	@Override
	public void dispose() {
		textVao.delete();
		textVbo.delete();
        shaderProgram.delete();
		
	}

	private void drawBoxTC(float x1, float y1, float x2, float y2, float s1, float t1, float s2, float t2, Vector3f c) {
		float r = c.x;
        float g = c.y;
        float b = c.z;

        vertices.put(x1).put(y1).put(r).put(g).put(b).put(s1).put(t1);
        vertices.put(x1).put(y2).put(r).put(g).put(b).put(s1).put(t2);
        vertices.put(x2).put(y2).put(r).put(g).put(b).put(s2).put(t2);

        vertices.put(x1).put(y1).put(r).put(g).put(b).put(s1).put(t1);
        vertices.put(x2).put(y2).put(r).put(g).put(b).put(s2).put(t2);
        vertices.put(x2).put(y1).put(r).put(g).put(b).put(s2).put(t1);
	
        numVertices += 6;
	}

	private void print(float x, float y, int font, String text) {
		xb.put(0, x);
		yb.put(0, y);

		debugFont.getChardata().position(font * 128 * STBTTPackedchar.SIZEOF);

		debugFont.getFontTexture().bind();

		for ( int i = 0; i < text.length(); i++ ) {
			stbtt_GetPackedQuad(debugFont.getChardata(), debugFont.getBitMapW(), debugFont.getBitMapH(), text.charAt(i), xb, yb, q.buffer(), 0);
			drawBoxTC(
				q.getX0(), q.getY0(), q.getX1(), q.getY1(),
				q.getS0(), q.getT0(), q.getS1(), q.getT1(),
				new Vector3f(1f,1f,1f)
			);
		}
	}
	
	

}
