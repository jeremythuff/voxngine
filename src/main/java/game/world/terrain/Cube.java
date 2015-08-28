package game.world.terrain;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_ENTER;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_ESCAPE;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.glfw.GLFW.glfwSetCursorPosCallback;
import static org.lwjgl.glfw.GLFW.glfwSetFramebufferSizeCallback;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;
import static org.lwjgl.glfw.GLFW.glfwSetMouseButtonCallback;
import static org.lwjgl.glfw.GLFW.glfwSetScrollCallback;
import static org.lwjgl.glfw.GLFW.glfwSetWindowShouldClose;
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
import org.joml.camera.ArcBallCamera;
import org.lwjgl.BufferUtils;
import org.lwjgl.glfw.GLFWCursorPosCallback;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWFramebufferSizeCallback;
import org.lwjgl.glfw.GLFWKeyCallback;
import org.lwjgl.glfw.GLFWMouseButtonCallback;
import org.lwjgl.glfw.GLFWScrollCallback;

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
//    private Quaternionf q = new Quaternionf();
    
    ArcBallCamera cam = new ArcBallCamera();
    
    GLFWKeyCallback keyCallback;
    GLFWFramebufferSizeCallback fbCallback;
    GLFWCursorPosCallback cpCallback;
    GLFWScrollCallback sCallback;
    GLFWMouseButtonCallback mbCallback;

    long window;
    int x, y;
    float zoom = 20;
    int mouseX, mouseY;
    boolean down;
    
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
        		   //Back
        		  -0.5f,  0.5f, -0.5f,	0, 0.6f, 0, 1, //top right
        		  -0.5f, -0.5f, -0.5f,	0.5f, 0.35f, 0.1f, 1, //bottom right
        		   0.5f, -0.5f, -0.5f,	0.5f, 0.35f, 0.1f, 1, //bottom left
        		   0.5f, -0.5f, -0.5f,	0.5f, 0.35f, 0.1f, 1, //bottom middle
        		   0.5f,  0.5f, -0.5f,	0, 0.6f, 0, 1, //top middle
        		  -0.5f,  0.5f, -0.5f,	0, 0.6f, 0, 1,//top left
        		  //left
        		  -0.5f, -0.5f,  0.5f,	0.5f, 0.35f, 0.1f, 1,//bottom right
        		  -0.5f, -0.5f, -0.5f,	0.5f, 0.35f, 0.1f, 1, //bottom left
        		  -0.5f,  0.5f, -0.5f,	0, 0.6f, 0, 1, //top left 
        		  -0.5f,  0.5f, -0.5f,	0, 0.6f, 0, 1, //top left
        		  -0.5f,  0.5f,  0.5f,	0, 0.6f, 0, 1, //top right
        		  -0.5f, -0.5f,  0.5f,	0.5f, 0.35f, 0.1f, 1, //top right
        		  
        		  //right
        		   0.5f, -0.5f, -0.5f,	0.5f, 0.35f, 0.1f, 1,
        		   0.5f, -0.5f,  0.5f,	0.5f, 0.35f, 0.1f, 1,
        		   0.5f,  0.5f,  0.5f,	0, 0.6f, 0, 1,
        		   0.5f,  0.5f,  0.5f,	0, 0.6f, 0, 1,
        		   0.5f,  0.5f, -0.5f,	0, 0.6f, 0, 1,
        		   0.5f, -0.5f, -0.5f,	0.5f, 0.35f, 0.1f, 1,
        		   
        		   //front
        		  -0.5f, -0.5f,  0.5f,	0.5f, 0.35f, 0.1f, 1, //bottom left
        		  -0.5f,  0.5f,  0.5f,	0, 0.6f, 0, 1, //top left
        		   0.5f,  0.5f,  0.5f,	0, 0.6f, 0, 1, //top middle 
        		   0.5f,  0.5f,  0.5f,	0, 0.6f, 0, 1, //top right
        		   0.5f, -0.5f,  0.5f,	0.5f, 0.35f, 0.1f, 1, //bottom right
        		  -0.5f, -0.5f,  0.5f,	0.5f, 0.35f, 0.1f, 1, //bottom middle
        		  //top
        		  -0.5f,  0.5f, -0.5f,	0, 0.8f, 0, 1,
        		   0.5f,  0.5f, -0.5f,	0, 0.8f, 0, 1,
        		   0.5f,  0.5f,  0.5f,	0, 0.8f, 0, 1,
        		   0.5f,  0.5f,  0.5f,	0, 0.8f, 0, 1,
        		  -0.5f,  0.5f,  0.5f,	0, 0.8f, 0, 1,
        		  -0.5f,  0.5f, -0.5f,	0, 0.8f, 0, 1,
        		  //bottom
        		  -0.5f, -0.5f, -0.5f,	0.5f, 0.35f, 0.1f, 1,
        		  -0.5f, -0.5f,  0.5f,	0.5f, 0.35f, 0.1f, 1,
        		   0.5f, -0.5f, -0.5f,	0.5f, 0.35f, 0.1f, 1,
        		   0.5f, -0.5f, -0.5f,	0.5f, 0.35f, 0.1f, 1,
        		  -0.5f, -0.5f,  0.5f,	0.5f, 0.35f, 0.1f, 1,
        		   0.5f, -0.5f,  0.5f,	0.5f, 0.35f, 0.1f, 1
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
        final long offsetColor    = 3 * sizeOfFloat;

        // Setup pointers using 'stride' and 'offset' we calculated above
        glVertexAttribPointer(0, 3, GL_FLOAT, false, stride, offsetPosition);
        glVertexAttribPointer(1, 4, GL_FLOAT, false, stride, offsetColor);

        // Enable the vertex attribute locations
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);

        glBindVertexArray(0);
        
        glfwSetFramebufferSizeCallback(Window.id, fbCallback = new GLFWFramebufferSizeCallback() {
            @Override
            public void invoke(long window, int w, int h) {
                if (w > 0 && h > 0) {
                	Window.WIDTH = w;
                    Window.HEIGHT = h;
                }
            }
        });
        glfwSetCursorPosCallback(Window.id, cpCallback = new GLFWCursorPosCallback() {
            @Override
            public void invoke(long window, double xpos, double ypos) {
                x = (int) xpos - Window.WIDTH / 2;
                y = Window.HEIGHT / 2 - (int) ypos;
            }
        });
        glfwSetMouseButtonCallback(Window.id, mbCallback = new GLFWMouseButtonCallback() {
            @Override
            public void invoke(long window, int button, int action, int mods) {
                if (action == GLFW_PRESS) {
                    down = true;
                    mouseX = x;
                    mouseY = y;
                } else if (action == GLFW_RELEASE) {
                    down = false;
                }
            }
        });
        glfwSetScrollCallback(Window.id, sCallback = new GLFWScrollCallback() {
            @Override
            public void invoke(long window, double xoffset, double yoffset) {
                if (yoffset > 0) {
                    zoom /= 1.1f;
                } else {
                    zoom *= 1.1f;
                }
            }
        });
        
        cam.setAlpha((float) Math.toRadians(-20));
        cam.setBeta((float) Math.toRadians(20));

        		
	}
	
	@Override
	public void input() {
		// TODO Auto-generated method stub
		
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
                (float) Window.WIDTH / Window.HEIGHT, 0.01f, 100.0f);
		
	}

	@Override
	public void render(RenderEngine renderEngine) {
		
		glEnable(GL_DEPTH_TEST);
        // Use our program
        shaderProgram.bind();
        
        // Bind the vertex array and enable our location
        glBindVertexArray(vaoID);
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
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoID);

        // Dispose the buffer object
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glDeleteBuffers(vboID);
		
	}

}
