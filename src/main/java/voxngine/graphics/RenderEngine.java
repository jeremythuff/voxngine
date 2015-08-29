package voxngine.graphics;

import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import voxngine.camera.TPCamera;
import voxngine.graphics.shaders.Shader;
import voxngine.graphics.shaders.ShaderProgram;

public class RenderEngine {
	
	private static List<FloatBuffer> buffers = new ArrayList<FloatBuffer>();
	
	private static ShaderProgram shaderProgram;
	private static Vao vao = new Vao();
	private static int matLocation;
	
	private static TPCamera cam = new TPCamera();
	
	private RenderEngine() {
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
		
	}
	
	public static void queBuffer(FloatBuffer vbo) {
		buffers.add(vbo);
	}
	
	public static void initVbos() {
		buildVbos();
	}
	
	public static void updateVbos() {
		buildVbos();
	}
	
	private static void buildVbos() {
		
		for(FloatBuffer interleavedBuffer : buffers) {
    		
			// Create a Buffer Object and upload the vertices buffer   
	        Vbo vbo = new Vbo();
	        vbo.bind(GL_ARRAY_BUFFER);
	        vbo.uploadData(GL_ARRAY_BUFFER, interleavedBuffer, GL_STATIC_DRAW);
	        
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
	      
	        vao.bind();
	        // Setup pointers using 'stride' and 'offset' we calculated above
	        glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, offsetPosition);
	        glVertexAttribPointer(1, 4, GL_FLOAT, false, stride, offsetColor);

	        // Enable the vertex attribute locations
	        glEnableVertexAttribArray(0);
	        glEnableVertexAttribArray(1);
	   
	        vao.unbind();
	        vbo.delete();
	        
		}
		
	}
	
	public static void input() {
		cam.input();
	}

	public static void update(float delta) {
		cam.update(delta);
	}
	
	public static void render() {
		
		glEnable(GL_DEPTH_TEST);
		
        // Use our program
        shaderProgram.bind();
        
        // Bind the vertex array and enable our location
        vao.bind();
        
        glUniformMatrix4fv(matLocation, false, cam.getVPMatrix());
        
        glDrawArrays(GL_TRIANGLES, 0, 36);
        
        // Disable our location
        vao.unbind();
        glBindVertexArray(0);
        
        // Un-bind our program
        shaderProgram.unbind();
	}
	
	public static void dispose() {
		vao.delete();
		shaderProgram.delete();
		
	}
	
	public static void init() {
		new RenderEngine();
	}
	
}
