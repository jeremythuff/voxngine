package game.world.terrain;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_STATIC_DRAW;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.camera.ArcBallCamera;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;

import game.world.WorldObject;
import voxngine.graphics.RenderEngine;
import voxngine.graphics.Vao;
import voxngine.graphics.Vbo;
import voxngine.graphics.shaders.Shader;
import voxngine.graphics.shaders.ShaderProgram;
import voxngine.io.Keyboard;
import voxngine.io.Mouse;
import voxngine.io.Window;

public class Cube implements WorldObject {
	
	private ShaderProgram shaderProgram;
    
    private Vao vao;
    private Vbo vbo;
    
    Matrix4f viewProjMatrix = new Matrix4f();
    FloatBuffer fb = BufferUtils.createFloatBuffer(16);
    int matLocation;
    
    ArcBallCamera cam = new ArcBallCamera();
    
    GLFWFramebufferSizeCallback fbCallback;

	boolean wireframe;

    int x, y;
    float zoom = 20;
    int mouseX, mouseY;
    boolean down;
    
    float cubeSize = 1f;
    
	@Override
	public void init() {
		
		shaderProgram = new ShaderProgram();
        
        Shader vShad = shaderProgram.loadShader(GL_VERTEX_SHADER, "src/main/resources/shaders/default.vs");
        shaderProgram.attachShader(vShad);
        vShad.delete();
        
        Shader fShad = shaderProgram.loadShader(GL_FRAGMENT_SHADER, "src/main/resources/shaders/default.fs");
        shaderProgram.attachShader(fShad);
        fShad.delete();
        
        shaderProgram.link();
        
        matLocation = shaderProgram.getUniformLocation("viewProjMatrix");
     
        // Create a FloatBuffer of vertices
        
        float[] vertices = new CubeGeometry().getGeometry(new Vector3f(0f,0f,0f));
        
        FloatBuffer interleavedBuffer = BufferUtils.createFloatBuffer(vertices.length);
        interleavedBuffer.put(vertices).flip();
        
        // Create a Buffer Object and upload the vertices buffer
        vbo = new Vbo();
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
        
        // Generate and bind a Vertex Array
        vao = new Vao();
        vao.bind();

        // Setup pointers using 'stride' and 'offset' we calculated above
        glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, offsetPosition);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, stride, offsetColor);

        // Enable the vertex attribute locations
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        vao.unbind();
        
        cam.setAlpha((float) Math.toRadians(-20));
        cam.setBeta((float) Math.toRadians(20));

        		
	}
	
	@Override
	public void input() {
		
		if(Keyboard.activeKeyEvent()) {
			if(Keyboard.isKeyDown(GLFW_KEY_F)) {
				if(wireframe) {
					wireframe = false;
					glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
				} else {
					glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
					wireframe = true;
				}
			}
		}
		
		if(Mouse.activeMoveEvent()) {
			x = (int) Mouse.getPos().x - Window.WIDTH / 2;
			y = Window.HEIGHT / 2 - (int) Mouse.getPos().y;
		}
		
		if(Mouse.activeClickEvent()) {
			if (Mouse.actionIs(GLFW_PRESS)) {
				down = true;
				mouseX = x;
				mouseY = y;
			} else if (Mouse.actionIs(GLFW_RELEASE)) {
				down = false;
			}
		}
		
		if(Mouse.activeScrollEvent()) {
			if (Mouse.getScrollDelta().y > 0) {
				zoom /= 1.1f;
			} else {
			    zoom *= 1.1f;
			}
		}
		
	}

	@Override
	public void update(float delta) {
		
		 /* Set input values for the camera */
        if (down) {
            cam.setAlpha(cam.getAlpha() + Math.toRadians((x - mouseX) * 0.5f));
            cam.setBeta(cam.getBeta() + Math.toRadians((mouseY - y) * 0.5f));
            mouseX = x;
            mouseY = y;
        }
        
        cam.zoom(zoom);
        cam.update(delta);
		
		cam.viewMatrix(viewProjMatrix).setPerspective((float) Math.atan((32.5 * Window.HEIGHT / 1200) / 60.0),
                (float) Window.WIDTH / Window.HEIGHT, 0.01f, 1000.0f);
		
	}

	@Override
	public void render() {
		RenderEngine.render();
		glEnable(GL_DEPTH_TEST);
        // Use our program
        shaderProgram.bind();
        
        // Bind the vertex array and enable our location
        glBindVertexArray(vao.getID());
        glEnableVertexAttribArray(0);
        glUniformMatrix4fv(matLocation, false, cam.viewMatrix(viewProjMatrix).get(fb));
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
        vao.delete();

        // Dispose the buffer object
        vbo.delete();
		
	}

}
