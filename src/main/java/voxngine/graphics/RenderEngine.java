package voxngine.graphics;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.stb.STBTruetype.stbtt_GetPackedQuad;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.Map;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.lwjgl.BufferUtils;
import org.lwjgl.stb.STBTTAlignedQuad;
import org.lwjgl.stb.STBTTPackedchar;

import voxngine.camera.Camera;
import voxngine.camera.TPCamera;
import voxngine.graphics.shaders.Shader;
import voxngine.graphics.shaders.ShaderProgram;
import voxngine.graphics.textures.Font;
import voxngine.io.Controlls;
import voxngine.io.ScreenMessage;
import voxngine.io.Window;

public class RenderEngine {
	
	int count;
	
	private int numVertices;
		
    private Vao textVao;
    private Vbo textVbo;
    
    private FloatBuffer textVertices;
	
	private Map<Integer, FloatBuffer> bufferMap = new HashMap<Integer, FloatBuffer>();
	private Map<Integer, Vao> vaos = new HashMap<Integer, Vao>();
	
	private ShaderProgram textShaderProgram;
	private ShaderProgram shaderProgram;
	private int matLocation;
	
	private Camera cam = new TPCamera();
	static boolean wireframe;

	
	//private Font debugFont;
	
	private final STBTTAlignedQuad q  = new STBTTAlignedQuad();
	private final FloatBuffer      xb = BufferUtils.createFloatBuffer(1);
	private final FloatBuffer      yb = BufferUtils.createFloatBuffer(1);
	
	public void queBuffer(int entityCount, FloatBuffer buffer) {
		bufferMap.put(entityCount, buffer);
	}
	
	public void initVbos() {
		
		shaderProgram = new ShaderProgram();
        
		Shader vShad = shaderProgram.loadShader(GL_VERTEX_SHADER, "src/main/resources/shaders/default.vs");
		shaderProgram.attachShader(vShad);
		vShad.delete();
		
		Shader fShad = shaderProgram.loadShader(GL_FRAGMENT_SHADER, "src/main/resources/shaders/default.fs");
		shaderProgram.attachShader(fShad);
		fShad.delete();
		
		shaderProgram.link();
		
		matLocation = shaderProgram.getUniformLocation("viewProjMatrix");
		
		cam.init();
		
		buildVbos();
	}
	
	public void initText() {
		textVao = new Vao();
		
		textVbo = new Vbo();
		
		 /* Create FloatBuffer */
        textVertices = BufferUtils.createFloatBuffer(4096);

        textVao.bind();
        
        /* Upload null data to allocate storage for the VBO */
        long size = textVertices.capacity() * Float.BYTES;
        textVbo.bind(GL_ARRAY_BUFFER);
        textVbo.uploadData(GL_ARRAY_BUFFER, size, GL_DYNAMIC_DRAW);
        
        /* Initialize variables */
        numVertices = 0;
        
        textShaderProgram = new ShaderProgram();
        
		Shader vShad = textShaderProgram.loadShader(GL_VERTEX_SHADER, "src/main/resources/shaders/text.vs");
		textShaderProgram.attachShader(vShad);
		vShad.delete();
		
		Shader fShad = textShaderProgram.loadShader(GL_FRAGMENT_SHADER, "src/main/resources/shaders/text.fs");
		textShaderProgram.attachShader(fShad);
		fShad.delete();
				
		textShaderProgram.link();
		textShaderProgram.bind();
		
		 /* Specify Vertex Pointer */
        int posAttrib = textShaderProgram.getAttributeLocation("fontPos");
        textShaderProgram.enableVertexAttribute(posAttrib);
        textShaderProgram.pointVertexAttribute(posAttrib, 2, 7 * Float.BYTES, 0);

        /* Specify Color Pointer */
        int colAttrib = textShaderProgram.getAttributeLocation("fontColor");
        textShaderProgram.enableVertexAttribute(colAttrib);
        textShaderProgram.pointVertexAttribute(colAttrib, 3, 7 * Float.BYTES, 2 * Float.BYTES);

        /* Specify Texture Pointer */
        int texAttrib = textShaderProgram.getAttributeLocation("fontTexcoord");
        textShaderProgram.enableVertexAttribute(texAttrib);
        textShaderProgram.pointVertexAttribute(texAttrib, 2, 7 * Float.BYTES, 5 * Float.BYTES);

        /* Set texture uniform */
        int uniTex = textShaderProgram.getUniformLocation("texImage");
        textShaderProgram.setUniform(uniTex, 0);

        /* Set model matrix to identity matrix */
        Matrix4f model = new Matrix4f();
        int uniModel = textShaderProgram.getUniformLocation("model");
        textShaderProgram.setUniform(uniModel, model);

        /* Set view matrix to identity matrix */
        Matrix4f view = new Matrix4f();
        int uniView = textShaderProgram.getUniformLocation("view");
        textShaderProgram.setUniform(uniView, view);

        /* Set projection matrix to an orthographic projection */
        Matrix4f projection = new Matrix4f();
        int uniProjection = textShaderProgram.getUniformLocation("projection");
        textShaderProgram.setUniform(uniProjection, projection.ortho(0.0f, Window.WIDTH, Window.HEIGHT, 0.0f, -1.0f, 1.0f));
        		
		count = 0;
	}
	
	public void updateVbos() {
		buildVbos();
	}
	
	private void buildVbos() {
		
		bufferMap.keySet().stream().forEach(key -> {
			
			FloatBuffer buffer = bufferMap.get(key);
			
			Vao vao = new Vao();
			vao.bind();
			vaos.put(key, vao);
			vao.bind();
			
			// Create a Buffer Object and upload the vertices buffer   
	        Vbo vbo = new Vbo();
	        vbo.bind(GL_ARRAY_BUFFER);
	        vbo.uploadData(GL_ARRAY_BUFFER, buffer, GL_STATIC_DRAW);
	        
	        // The size of float, in bytes (will be 4)
	        final int sizeOfFloat = Float.SIZE / Byte.SIZE;

	        // The sizes of the vertex and color components
	        final int vertexSize = 3 * sizeOfFloat;
	        final int colorSize  = 4 * sizeOfFloat;

	        // The 'stride' is the sum of the sizes of individual components
	        final int stride = vertexSize + colorSize;

	        // The 'offset is the number of bytes from the start of the tuple
	        final long offsetPosition = 0;
	        final long offsetColor    = 3 * sizeOfFloat;
	        
	        // Setup pointers using 'stride' and 'offset' we calculated above
	        glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, offsetPosition);
	        glVertexAttribPointer(1, 4, GL_FLOAT, false, stride, offsetColor);

	        // Enable the vertex attribute locations
	        glEnableVertexAttribArray(0);
	        glEnableVertexAttribArray(1);
	        vao.unbind();
	        //break;
			
		});
		
	}
	
	public void input(Controlls controlls) {
		cam.input(controlls);
		
		if(controlls.getKeyboad().isKeyDown(GLFW_KEY_F)) {
			if(wireframe) {
				wireframe = false;
			} else {
				wireframe = true;
			}			
		}
	}

	public void update(float delta) {
		cam.update(delta);
		
	}
	
	
	public void render() {
		render3d();
		renderText();
	}
	
	private void render3d() {
		
		glEnable(GL_DEPTH_TEST);
		
		if(wireframe) {
			glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		} else {
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		}
		
		FloatBuffer fb = cam.getMVMatrix();
		
		bufferMap.keySet().stream().forEach(entityCount -> {	
			vaos.get(entityCount).bind();
            shaderProgram.bind();
            glUniformMatrix4fv(matLocation, false, fb);
        	glDrawArrays(GL_TRIANGLES, 0, 36*entityCount);
            shaderProgram.unbind();
            vaos.get(entityCount).unbind();
            
        });
	}
	
	private void renderText() {
		
		if (numVertices > 0) {
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
            textVertices.flip();
            textVao.bind();
            textShaderProgram.bind();
            /* Upload the new vertex data */
            textVbo.bind(GL_ARRAY_BUFFER);
            textVbo.uploadSubData(GL_ARRAY_BUFFER, 0, textVertices);
            /* Draw batch */
            glDrawArrays(GL_TRIANGLES, 0, numVertices);
            /* Clear vertex data for next batch */
            numVertices = 0;
        }
		
		textVertices.clear();
	}
	
	public Camera getCamera() {
		return this.cam;
	}
	
	public void dispose() {
		 for(int key : vaos.keySet()) { 
			 vaos.get(key).delete();
		 }
		if(shaderProgram!=null)shaderProgram.delete();
		
		textVao.delete();
		textVbo.delete();
		if(textShaderProgram!=null)textShaderProgram.delete();
		
	}
	
	private void drawBoxTC(float x1, float y1, float x2, float y2, float s1, float t1, float s2, float t2, Vector3f c) {
		float r = c.x;
        float g = c.y;
        float b = c.z;

        textVertices.put(x1).put(y1).put(r).put(g).put(b).put(s1).put(t1);
        textVertices.put(x1).put(y2).put(r).put(g).put(b).put(s1).put(t2);
        textVertices.put(x2).put(y2).put(r).put(g).put(b).put(s2).put(t2);

        textVertices.put(x1).put(y1).put(r).put(g).put(b).put(s1).put(t1);
        textVertices.put(x2).put(y2).put(r).put(g).put(b).put(s2).put(t2);
        textVertices.put(x2).put(y1).put(r).put(g).put(b).put(s2).put(t1);
	
        numVertices += 6;
	}

	public void print(float x, float y, Font font, String text) {
		xb.put(0, x);
		yb.put(0, y);

		font.getChardata().position(font.getFontTexture().getId() * 128 * STBTTPackedchar.SIZEOF);

		font.getFontTexture().bind();

		for ( int i = 0; i < text.length(); i++ ) {
			stbtt_GetPackedQuad(font.getChardata(), font.getBitMapW(), font.getBitMapH(), text.charAt(i), xb, yb, q.buffer(), 0);
			drawBoxTC(
				q.getX0(), q.getY0(), q.getX1(), q.getY1(),
				q.getS0(), q.getT0(), q.getS1(), q.getT1(),
				new Vector3f(1f,1f,1f)
			);
		}
	}
	
}
