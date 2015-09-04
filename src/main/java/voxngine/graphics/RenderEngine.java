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
import java.util.HashMap;
import java.util.Map;

import voxngine.camera.TPCamera;
import voxngine.graphics.shaders.Shader;
import voxngine.graphics.shaders.ShaderProgram;
import voxngine.io.Controlls;

public class RenderEngine {
	
	private Map<Integer, FloatBuffer> bufferMap = new HashMap<Integer, FloatBuffer>();
	private Map<Integer, Vao> vaos = new HashMap<Integer, Vao>();
	
	private ShaderProgram shaderProgram;
	private int matLocation;
	
	private TPCamera cam = new TPCamera();
	
	public RenderEngine() {
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
	
	public void queBuffer(int entityCount, FloatBuffer buffer) {
		bufferMap.put(entityCount, buffer);
	}
	
	public void initVbos() {
		buildVbos();
	}
	
	public void updateVbos() {
		buildVbos();
	}
	
	private void buildVbos() {
		
		for(int key : bufferMap.keySet()) {
			
			FloatBuffer buffer = bufferMap.get(key);
			
			Vao vao = new Vao();
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
		}
		
	}
	
	public void input(Controlls controlls) {
		cam.input(controlls);
	}

	public void update(float delta) {
		cam.update(delta);
	}
	
	public void render() {
		
		glEnable(GL_DEPTH_TEST);
		
		FloatBuffer fb = cam.getMVMatrix();
		
        for(int entityCount : bufferMap.keySet()) {
			vaos.get(entityCount).bind();
            shaderProgram.bind();
            glUniformMatrix4fv(matLocation, false, fb);
        	glDrawArrays(GL_TRIANGLES, 0, 36*entityCount);
            shaderProgram.unbind();
            vaos.get(entityCount).unbind();
        }
	}
	
	public void dispose() {
		 for(int key : vaos.keySet()) { 
			 vaos.get(key).delete();
		 }
		shaderProgram.delete();
		
	}
	
}
