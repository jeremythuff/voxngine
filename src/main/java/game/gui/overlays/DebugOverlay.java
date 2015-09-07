package game.gui.overlays;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

import org.joml.Matrix4f;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTPackContext;
import org.lwjgl.stb.STBTTPackedchar;
import org.lwjgl.system.MemoryUtil;

import game.gui.GuiObject;
import voxngine.graphics.RenderEngine;
import voxngine.graphics.Vao;
import voxngine.graphics.Vbo;
import voxngine.graphics.shaders.Shader;
import voxngine.graphics.shaders.ShaderProgram;
import voxngine.graphics.textures.Texture;
import voxngine.io.Controlls;
import voxngine.io.Window;
import voxngine.utils.FileUtils;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.stb.STBTruetype.stbtt_GetPackedQuad;
import static org.lwjgl.stb.STBTruetype.stbtt_PackBegin;
import static org.lwjgl.stb.STBTruetype.stbtt_PackEnd;
import static org.lwjgl.stb.STBTruetype.stbtt_PackFontRange;
import static org.lwjgl.stb.STBTruetype.stbtt_PackSetOversampling;


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

	private final int[] sf = {
		0, 1, 2,
		0, 1, 2,
	};

	// ----

	private final STBTTAlignedQuad q  = new STBTTAlignedQuad();
	private final FloatBuffer      xb = BufferUtils.createFloatBuffer(1);
	private final FloatBuffer      yb = BufferUtils.createFloatBuffer(1);
	
	private Texture font_tex;

	private ByteBuffer chardata;

	private int font = 3;
	
	private double mousex = 0;
	private double mousey = 0;

	private boolean show_tex;

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
	public void update(float delta) {
		// TODO Auto-generated method stub
	}

	@Override
	public void render(RenderEngine renderer) {
		
//		glEnable(GL_BLEND);
//		glBlendFunc(GL_SRC_COLOR, GL_ONE_MINUS_SRC_COLOR);
		
		glEnable(GL_BLEND);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
		
		if (numVertices > 0) {
            vertices.flip();

             vao.bind();

            shaderProgram.bind();

            /* Upload the new vertex data */
            vbo.bind(GL_ARRAY_BUFFER);
            vbo.uploadSubData(GL_ARRAY_BUFFER, 0, vertices);

            /* Draw batch */
            glDrawArrays(GL_TRIANGLES, 0, numVertices);

            /* Clear vertex data for next batch */
            vertices.clear();
            numVertices = 0;
        }
	}

	@Override
	public void dispose() {
		vao.delete();
		vbo.delete();
        shaderProgram.delete();
		
	}

	@Override
	public void input(Controlls controlls) {
		print(100f, 100f, font_tex.getId(), "Mouse X: "+(int)controlls.getMouse().getPos().x);
		print(100f, 120f, font_tex.getId(), "Mouse Y: "+(int)controlls.getMouse().getPos().y);
		
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
			
			
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void drawBoxTC(float x1, float y1, float x2, float y2, float s1, float t1, float s2, float t2) {
		float r = 1.0f;//c.getRed() / 255f;
        float g = 1.0f;//c.getGreen() / 255f;
        float b = 1.0f;//c.getBlue() / 255f;

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
				q.getS0(), q.getT0(), q.getS1(), q.getT1()
			);
		}
	}
	

}
