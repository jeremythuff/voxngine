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

import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

import voxngine.camera.TPCamera;
import voxngine.graphics.shaders.Shader;
import voxngine.graphics.shaders.ShaderProgram;

public class RenderEngine {
	
	private static List<FloatBuffer> buffers = new ArrayList<FloatBuffer>();
	private static List<Vao> vaos = new ArrayList<Vao>();
	
	private static ShaderProgram shaderProgram;
	//private static Vao vao = new Vao();
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
	
	public static void queBuffer(FloatBuffer buffer) {
		buffers.add(buffer);
	}
	
	public static void initVbos() {
		buildVbos();
	}
	
	public static void updateVbos() {
		buildVbos();
	}
	
	private static void buildVbos() {
		
		//for(FloatBuffer interleavedBuffer : buffers) {
			Vao vao = new Vao();
			vaos.add(vao);
			vao.bind();
			
			// Create a Buffer Object and upload the vertices buffer   
	        Vbo vbo = new Vbo();
	        vbo.bind(GL_ARRAY_BUFFER);
	        vbo.uploadData(GL_ARRAY_BUFFER, buffers.get(0), GL_STATIC_DRAW);
	        
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
		//}
		
	}
	
	public static void input() {
		cam.input();
	}

	public static void update(float delta) {
		cam.update(delta);
	}
	
	public static void render() {
		
		glEnable(GL_DEPTH_TEST);
		
        //for(Vao vao : vaos) {
			vaos.get(0).bind();
            shaderProgram.bind();
            glUniformMatrix4fv(matLocation, false, cam.getVPMatrix());
        	glDrawArrays(GL_TRIANGLES, 0, 36*9000);
            shaderProgram.unbind();
            vaos.get(0).unbind();
        //}
	}
	
	public static void dispose() {
		 for(Vao vao : vaos) { 
			 vao.delete();
		 }
		 
		vaos.get(0).delete();;
		shaderProgram.delete();
		
	}
	
	public static void init() {
		new RenderEngine();
	}
	
}
