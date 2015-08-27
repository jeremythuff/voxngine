package game.world.terrain;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL15.glBindBuffer;
import static org.lwjgl.opengl.GL15.glBufferData;
import static org.lwjgl.opengl.GL15.glDeleteBuffers;
import static org.lwjgl.opengl.GL15.glGenBuffers;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.lwjgl.BufferUtils;

import game.world.WorldObject;
import voxngine.graphics.RenderEngine;
import voxngine.graphics.shaders.Shader;
import voxngine.graphics.shaders.ShaderProgram;
import voxngine.io.Window;

public class Cube implements WorldObject {
	
	private ShaderProgram shaderProgram;
    private int vaoID;
    private int vboID;
    
    Matrix4f viewProjMatrix = new Matrix4f();
    FloatBuffer fb = BufferUtils.createFloatBuffer(16);
    int matLocation;
    
    /* Quaternion to rotate the cube */
    private Quaternionf q = new Quaternionf();
    
	@Override
	public void init() {
		
		shaderProgram = new ShaderProgram();
        
        Shader vShad = shaderProgram.loadShader(GL_VERTEX_SHADER, "src/main/resources/shaders/cube.vs");
        shaderProgram.attachShader(vShad);
        vShad.delete();
        
        Shader fShad = shaderProgram.loadShader(GL_FRAGMENT_SHADER, "src/main/resources/shaders/cube.fs");
        shaderProgram.attachShader(fShad);
        fShad.delete();
        
        shaderProgram.link();
        
        matLocation = shaderProgram.getUniformLocation("viewProjMatrix");

        System.out.println(matLocation);
        
        // Generate and bind a Vertex Array
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);
        
        float vertices[] = {
        		  -0.5f,  0.5f, -0.5f,	1, 0, 0, 1,
        		  -0.5f, -0.5f, -0.5f,	1, 0, 0, 1,
        		   0.5f, -0.5f, -0.5f,	1, 0, 0, 1,
        		   0.5f, -0.5f, -0.5f,	1, 0, 0, 1,
        		   0.5f,  0.5f, -0.5f,	1, 0, 0, 1,
        		  -0.5f,  0.5f, -0.5f,	1, 0, 0, 1,
        		  
        		  -0.5f, -0.5f,  0.5f,	0, 1, 0, 1,
        		  -0.5f, -0.5f, -0.5f,	0, 1, 0, 1,
        		  -0.5f,  0.5f, -0.5f,	0, 1, 0, 1,
        		  -0.5f,  0.5f, -0.5f,	0, 1, 0, 1,
        		  -0.5f,  0.5f,  0.5f,	0, 1, 0, 1,
        		  -0.5f, -0.5f,  0.5f,	0, 1, 0, 1,
        		  
        		   0.5f, -0.5f, -0.5f,	0, 0, 1, 1,
        		   0.5f, -0.5f,  0.5f,	0, 0, 1, 1,
        		   0.5f,  0.5f,  0.5f,	0, 0, 1, 1,
        		   0.5f,  0.5f,  0.5f,	0, 0, 1, 1,
        		   0.5f,  0.5f, -0.5f,	0, 0, 1, 1,
        		   0.5f, -0.5f, -0.5f,	0, 0, 1, 1,
        		   
        		   //front
        		  -0.5f, -0.5f,  0.5f,	0, 1, 0, 1,
        		  -0.5f,  0.5f,  0.5f,	0, 1, 1, 1,
        		   0.5f,  0.5f,  0.5f,	0, 1, 1, 1,
        		   0.5f,  0.5f,  0.5f,	0, 1, 1, 1,
        		   0.5f, -0.5f,  0.5f,	0, 1, 1, 1,
        		  -0.5f, -0.5f,  0.5f,	0, 1, 1, 1,
        		  
        		  -0.5f,  0.5f, -0.5f,	1, 0, 1, 1,
        		   0.5f,  0.5f, -0.5f,	1, 0, 1, 1,
        		   0.5f,  0.5f,  0.5f,	1, 0, 1, 1,
        		   0.5f,  0.5f,  0.5f,	1, 0, 1, 1,
        		  -0.5f,  0.5f,  0.5f,	1, 0, 1, 1,
        		  -0.5f,  0.5f, -0.5f,	1, 0, 1, 1,
        		  
        		  -0.5f, -0.5f, -0.5f,	1, 1, 0, 1,
        		  -0.5f, -0.5f,  0.5f,	1, 1, 0, 1,
        		   0.5f, -0.5f, -0.5f,	1, 1, 0, 1,
        		   0.5f, -0.5f, -0.5f,	1, 1, 0, 1,
        		  -0.5f, -0.5f,  0.5f,	1, 1, 0, 1,
        		   0.5f, -0.5f,  0.5f,	1, 1, 0, 1
        		};

     // Create a FloatBuffer of vertices
        FloatBuffer interleavedBuffer = BufferUtils.createFloatBuffer(vertices.length);
        interleavedBuffer.put(vertices).flip();

        // Create a Buffer Object and upload the vertices buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, interleavedBuffer, GL_STATIC_DRAW);

        // The size of float, in bytes (will be 4)
        final int sizeOfFloat = Float.SIZE / Byte.SIZE;

        // The sizes of the vertex and color components
        final int vertexSize = 3 * sizeOfFloat;
        final int colorSize  = 4 * sizeOfFloat;

        // The 'stride' is the sum of the sizes of individual components
        final int stride = vertexSize + colorSize;

        // The 'offset is the number of bytes from the start of the tuple
        final long offsetPosition = 0;
        final long offsetColor    = 3;

        // Setup pointers using 'stride' and 'offset' we calculated above
        glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, offsetPosition);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, stride, offsetColor);

        // Enable the vertex attribute locations
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindVertexArray(0);
        		
	}
	
	@Override
	public void input() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float delta) {
		
        viewProjMatrix.setPerspective((float) Math.atan((32.5 * Window.HEIGHT / 1200) / 60.0),
                                      (float) Window.WIDTH / Window.HEIGHT, 0.01f, 100.0f)
                      .lookAt(0.0f, 4.0f, 10.0f,
                              0.0f, 0.5f, 0.0f,
                              0.0f, 1.0f, 0.0f)
                      .get(fb);
		
        viewProjMatrix.translate(0.0f, 0.5f, 0.0f)
                      .rotate(q.rotateY((float) Math.toRadians(45) * delta))
                      .get(fb);
	}

	@Override
	public void render(RenderEngine renderEngine) {
		
		glEnable(GL_DEPTH_TEST);
        // Use our program
        shaderProgram.bind();
        
        // Bind the vertex array and enable our location
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);
        glUniformMatrix4fv(matLocation, false, fb);
        // Draw a triangle of 3 vertices
        glDrawArrays(GL_TRIANGLES, 0, 36);

        // Disable our location
        glDisableVertexAttribArray(0);
        glBindVertexArray(0);

        // Un-bind our program
        shaderProgram.unbind();
		
	}

	@Override
	public void dispose() {
		 // Dispose the program
        shaderProgram.delete();

        // Dispose the vertex array
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoID);

        // Dispose the buffer object
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboID);
		
	}

}
