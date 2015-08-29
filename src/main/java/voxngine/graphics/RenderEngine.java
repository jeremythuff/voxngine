package voxngine.graphics;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPolygonMode;
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

import org.joml.Matrix4f;
import org.joml.camera.ArcBallCamera;
import org.lwjgl.BufferUtils;

import voxngine.graphics.shaders.Shader;
import voxngine.graphics.shaders.ShaderProgram;
import voxngine.io.Keyboard;
import voxngine.io.Mouse;
import voxngine.io.Window;

public class RenderEngine {
	
	private static List<FloatBuffer> buffers = new ArrayList<FloatBuffer>();
	private static ShaderProgram shaderProgram;
	private static Vao vao = new Vao();
	private static Matrix4f viewProjMatrix = new Matrix4f();
	private static FloatBuffer fb = BufferUtils.createFloatBuffer(16);
	private static int matLocation;
	
	static boolean wireframe;

    static int x;
	static int y;
    static float zoom = 20;
    static int mouseX;
	static int mouseY;
    static boolean down;
	
	private static ArcBallCamera cam = new ArcBallCamera();
	
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
		cam.setAlpha((float) Math.toRadians(-20));
		cam.setBeta((float) Math.toRadians(20));
		
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
		}
		
	}
	
	public static void input() {
		
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

	public static void update(float delta) {
		
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
	
	public static void render() {
		
		glEnable(GL_DEPTH_TEST);
		
        // Use our program
        shaderProgram.bind();
        
        // Bind the vertex array and enable our location
        vao.bind();
        
        glUniformMatrix4fv(matLocation, false, cam.viewMatrix(viewProjMatrix).get(fb));
        glDrawArrays(GL_TRIANGLES, 0, 36);
        
        // Disable our location
        vao.unbind();
        glBindVertexArray(0);
        
        // Un-bind our program
        shaderProgram.unbind();
	}
	
	public static void init() {
		new RenderEngine();
	}
	
}
