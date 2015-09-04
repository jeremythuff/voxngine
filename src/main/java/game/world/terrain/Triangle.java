package game.world.terrain;

import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
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
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL30.glDeleteVertexArrays;
import static org.lwjgl.opengl.GL30.glGenVertexArrays;

import java.nio.FloatBuffer;

import org.lwjgl.BufferUtils;

import game.world.WorldObject;
import voxngine.graphics.RenderEngine;
import voxngine.graphics.shaders.Shader;
import voxngine.graphics.shaders.ShaderProgram;
import voxngine.io.Controlls;

public class Triangle implements WorldObject {
	
	private ShaderProgram shaderProgram;
    private int vaoID;
    private int vboID;

	@Override
	public void init(RenderEngine renderer) {
		
		shaderProgram = new ShaderProgram();
        
        Shader vShad = shaderProgram.loadShader(GL_VERTEX_SHADER, "src/main/resources/shaders/triangle.vs");
        shaderProgram.attachShader(vShad);
        vShad.delete();
        
        Shader fShad = shaderProgram.loadShader(GL_FRAGMENT_SHADER, "src/main/resources/shaders/triangle.fs");
        shaderProgram.attachShader(fShad);
        fShad.delete();
        
        shaderProgram.link();

        // Generate and bind a Vertex Array
        vaoID = glGenVertexArrays();
        glBindVertexArray(vaoID);

        // The vertices of our Triangle
        float[] vertices = new float[]
        {
            +0.0f, +0.8f,    // Top coordinate
            -0.8f, -0.8f,    // Bottom-left coordinate
            +0.8f, -0.8f     // Bottom-right coordinate
        };

        // Create a FloatBuffer of vertices
        FloatBuffer verticesBuffer = BufferUtils.createFloatBuffer(vertices.length);
        verticesBuffer.put(vertices).flip();

        // Create a Buffer Object and upload the vertices buffer
        vboID = glGenBuffers();
        glBindBuffer(GL_ARRAY_BUFFER, vboID);
        glBufferData(GL_ARRAY_BUFFER, verticesBuffer, GL_STATIC_DRAW);

        // Point the buffer at location 0, the location we set
        // inside the vertex shader. You can use any location
        // but the locations should match
        glVertexAttribPointer(0, 2, GL_FLOAT, false, 0, 0);
        glBindVertexArray(0);
		
	}
	
	@Override
	public void input(Controlls controlls) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(float delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(RenderEngine renderer) {

        // Use our program
        shaderProgram.bind();

        // Bind the vertex array and enable our location
        glBindVertexArray(vaoID);
        glEnableVertexAttribArray(0);

        // Draw a triangle of 3 vertices
        glDrawArrays(GL_TRIANGLES, 0, 3);

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
