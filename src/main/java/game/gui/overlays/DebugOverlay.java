package game.gui.overlays;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_LEFT_CONTROL;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_P;
import static org.lwjgl.opengl.GL11.GL_BLEND;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_ONE_MINUS_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_RED;
import static org.lwjgl.opengl.GL11.GL_SRC_ALPHA;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glBlendFunc;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.stb.STBTruetype.stbtt_GetPackedQuad;
import static org.lwjgl.stb.STBTruetype.stbtt_PackBegin;
import static org.lwjgl.stb.STBTruetype.stbtt_PackEnd;
import static org.lwjgl.stb.STBTruetype.stbtt_PackFontRange;
import static org.lwjgl.stb.STBTruetype.stbtt_PackSetOversampling;
import game.gui.GuiObject;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.util.List;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;

import voxngine.graphics.RenderEngine;
import voxngine.graphics.Vao;
import voxngine.graphics.Vbo;
import voxngine.graphics.shaders.Shader;
import voxngine.graphics.shaders.ShaderProgram;
import voxngine.graphics.textures.Texture;
import voxngine.io.Controlls;
import voxngine.io.ScreenMessage;
import voxngine.io.Window;
import voxngine.utils.FileUtils;


public class DebugOverlay implements GuiObject {
	
private ShaderProgram shaderProgram;
	
	int count;
    private int numVertices;
	
    private Vao vao;
    private Vbo vbo;
    
    private FloatBuffer vertices;

	private final int BITMAP_W = 512;
	private final int BITMAP_H = 512;
	
	private final float[] scale = {
		24.0f,
		14.0f
	};
	
	private final STBTTAlignedQuad q  = new STBTTAlignedQuad();
	private final FloatBuffer      xb = BufferUtils.createFloatBuffer(1);
	private final FloatBuffer      yb = BufferUtils.createFloatBuffer(1);
	
	private Texture font_tex;

	private ByteBuffer chardata;
	
	private float lineStart = 50f;
	
	private boolean show_tex = true;

	@Override
	public void init(RenderEngine renderer) {
		
		vao = new Vao();
		
		vbo = new Vbo();
		
		 /* Create FloatBuffer */
        vertices = BufferUtils.createFloatBuffer(4096);

        vao.bind();
        
        /* Upload null data to allocate storage for the VBO */
        long size = vertices.capacity() * Float.BYTES;
        vbo.bind(GL_ARRAY_BUFFER);
        vbo.uploadData(GL_ARRAY_BUFFER, size, GL_DYNAMIC_DRAW);
        
        /* Initialize variables */
        numVertices = 0;
        
        shaderProgram = new ShaderProgram();
        
		Shader vShad = shaderProgram.loadShader(GL_VERTEX_SHADER, "src/main/resources/shaders/text.vs");
		shaderProgram.attachShader(vShad);
		vShad.delete();
		
		Shader fShad = shaderProgram.loadShader(GL_FRAGMENT_SHADER, "src/main/resources/shaders/text.fs");
		shaderProgram.attachShader(fShad);
		fShad.delete();
		
		shaderProgram.bindFragmentDataLocation(0, "fragColor");
		
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
        
		load_fonts();
		
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
		if(Window.getScreenMessages("DebugOverlay") != null)
		for(int i = 0 ; i < Window.getScreenMessages("DebugOverlay").size() ; i++) {
			ScreenMessage screenMessage = Window.popMessage("DebugOverlay");
			
			if(screenMessage.getType().equals("GENERAL")) {
				print(50f, lineIncr, font_tex.getId(), screenMessage.getMessage());
				lineIncr += 20;
			} else if(screenMessage.getType().equals("TITLE")) {
				float center = (Window.WIDTH/2)-((screenMessage.getMessage().length()*12)/2);
				print(center, 50f, font_tex.getId(), screenMessage.getMessage());
			}
			
		}
	}

	@Override
	public void render(RenderEngine renderer) {
		
		if (numVertices > 0 && show_tex) {
            vertices.flip();

             vao.bind();

            shaderProgram.bind();

            /* Upload the new vertex data */
            vbo.bind(GL_ARRAY_BUFFER);
            vbo.uploadSubData(GL_ARRAY_BUFFER, 0, vertices);

            /* Draw batch */
            glDrawArrays(GL_TRIANGLES, 0, numVertices);

            /* Clear vertex data for next batch */
            
            numVertices = 0;
        }
		
		vertices.clear();
		
	}

	@Override
	public void dispose() {
		vao.delete();
		vbo.delete();
        shaderProgram.delete();
		
	}
	
	private void load_fonts() {
		chardata = BufferUtils.createByteBuffer(6 * 128 * STBTTPackedchar.SIZEOF);

		try {
			
			ByteBuffer ttf = FileUtils.ioResourceToByteBuffer("fonts/Cabin-Regular.ttf", 160 * 1024);

			ByteBuffer bitmap = BufferUtils.createByteBuffer(BITMAP_W * BITMAP_H);

			ByteBuffer pc = BufferUtils.createByteBuffer(STBTTPackContext.SIZEOF);
			stbtt_PackBegin(pc, bitmap, BITMAP_W, BITMAP_H, 0, 1, null);
			for ( int i = 0; i < 2; i++ ) {
				chardata.position(((i * 3 + 0) * 128 + 32) * STBTTPackedchar.SIZEOF);
				stbtt_PackSetOversampling(pc, 1, 1);
				stbtt_PackFontRange(pc, ttf, 0, scale[i], 32, 95, chardata);

				chardata.position(((i * 3 + 1) * 128 + 32) * STBTTPackedchar.SIZEOF);
				stbtt_PackSetOversampling(pc, 2, 2);
				stbtt_PackFontRange(pc, ttf, 0, scale[i], 32, 95, chardata);

				chardata.position(((i * 3 + 2) * 128 + 32) * STBTTPackedchar.SIZEOF);
				stbtt_PackSetOversampling(pc, 3, 1);
				stbtt_PackFontRange(pc, ttf, 0, scale[i], 32, 95, chardata);
			}
			stbtt_PackEnd(pc);

			font_tex = new Texture(BITMAP_W, BITMAP_H, GL_RED, bitmap);
			font_tex.bind();
			
			glEnable(GL_BLEND);
	        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
			
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
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

		chardata.position(font * 128 * STBTTPackedchar.SIZEOF);

		font_tex.bind();

		for ( int i = 0; i < text.length(); i++ ) {
			stbtt_GetPackedQuad(chardata, BITMAP_W, BITMAP_H, text.charAt(i), xb, yb, q.buffer(), 0);
			drawBoxTC(
				q.getX0(), q.getY0(), q.getX1(), q.getY1(),
				q.getS0(), q.getT0(), q.getS1(), q.getT1(),
				new Vector3f(1f,1f,1f)
			);
		}
	}
	

}
