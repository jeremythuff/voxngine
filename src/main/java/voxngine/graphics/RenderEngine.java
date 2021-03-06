package voxngine.graphics;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;
import static org.lwjgl.opengl.GL11.GL_CULL_FACE;
import static org.lwjgl.opengl.GL11.GL_DEPTH_TEST;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FLOAT;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.GL_TRIANGLES;
import static org.lwjgl.opengl.GL11.GL_UNSIGNED_INT;
import static org.lwjgl.opengl.GL11.glDisable;
import static org.lwjgl.opengl.GL11.glDrawArrays;
import static org.lwjgl.opengl.GL11.glDrawElements;
import static org.lwjgl.opengl.GL11.glEnable;
import static org.lwjgl.opengl.GL11.glPolygonMode;
import static org.lwjgl.opengl.GL15.GL_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL15.GL_DYNAMIC_DRAW;
import static org.lwjgl.opengl.GL15.GL_ELEMENT_ARRAY_BUFFER;
import static org.lwjgl.opengl.GL20.GL_FRAGMENT_SHADER;
import static org.lwjgl.opengl.GL20.GL_VERTEX_SHADER;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glUniformMatrix4fv;
import static org.lwjgl.opengl.GL20.glVertexAttribPointer;
import static org.lwjgl.stb.STBTruetype.stbtt_GetPackedQuad;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;

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
import voxngine.threading.NonBlockingExecutor;
import voxngine.threading.NonBlockingFuture;
import voxngine.utils.BufferManager;

public class RenderEngine {
	
	int count;
	
	private int numTextVertices;
	private int num3DVertices;
	private int totalDepictedEntities;
	
	private NonBlockingExecutor executor =
            new NonBlockingExecutor(Executors.newSingleThreadExecutor());
	
	private BufferManager bufferManager;
	
    private Vao textVao;
    private Rbo textVbo;

    private Map<Integer, Map<String, RenderObject>> renderObjects = new HashMap<Integer, Map<String, RenderObject>>();
    	
	private ShaderProgram textShaderProgram;
	private ShaderProgram shaderProgram;
	private int matLocation;
	
	private Camera cam = new TPCamera();
	static boolean wireframe;
	
    private FloatBuffer textVertices;	
	private final STBTTAlignedQuad q  = new STBTTAlignedQuad();
	private final FloatBuffer      xb = BufferUtils.createFloatBuffer(1);
	private final FloatBuffer      yb = BufferUtils.createFloatBuffer(1);
	
	public RenderEngine() {
		bufferManager = new BufferManager();
	}
	
	
	public int registerMesh(Mesh mesh) {
		
		Map<String, RenderObject> roMap = buildRenderObject(mesh);
		mesh.clear();
		int id = renderObjects.size();
		
		renderObjects.put(id, roMap);
				
		return id;
	}
	
	public void updateMesh(int id, Mesh mesh) {
		Map<String, RenderObject> newRoMap = buildRenderObject(mesh);
		renderObjects.replace(id, renderObjects.get(id), newRoMap);
		mesh.clear();
	}
	
	public void updateDepictedEntityCount(int count) {
		totalDepictedEntities += count;
	}

	
	private Map<String, RenderObject> buildRenderObject(Mesh mesh) {
		
		Vao vao = new Vao();
		vao.setCount(mesh.getEntityCount());
		vao.bind();
				
		FloatBuffer verticesBuffer = bufferManager.getFloatBuffer();
		verticesBuffer.put(mesh.getVertArray());		
		verticesBuffer.flip();
		
		// Create a Buffer Object and upload the vertices buffer   
        Rbo vbo = new Rbo(GL_ARRAY_BUFFER);
        vbo.bind();
        vbo.uploadData(verticesBuffer, GL_DYNAMIC_DRAW);
        vbo.setCount(mesh.getIndecesArray().length);
        
        bufferManager.releaseFLoatBuffer(verticesBuffer);
        
        IntBuffer entityBuffer = bufferManager.getIntBuffer();
        entityBuffer.put(mesh.getIndecesArray());		
        entityBuffer.flip();
        
        // Create a Buffer Object and upload the vertices buffer   
        Rbo ebo = new Rbo(GL_ELEMENT_ARRAY_BUFFER);
        ebo.bind();
        ebo.uploadData(entityBuffer, GL_DYNAMIC_DRAW);
        ebo.setCount(mesh.getEntityCount());
        
        bufferManager.releaseIntBuffer(entityBuffer);
        
        mesh.clear();
        
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
        //glVertexAttribPointer(2, 3, GL_FLOAT, false, stride, offsetPosition);

		 // Enable the vertex attribute locations
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        //glEnableVertexAttribArray(2);
        
        vao.unbind();
		
		Map<String, RenderObject> rboMap = new HashMap<String, RenderObject>();
		
		rboMap.put("VAO", vao);
		rboMap.put("VBO", vbo);
		rboMap.put("EBO", ebo);
		
		return rboMap;
	}
	
	public void initShaders() {
		
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
	
	public void initText() {
		textVao = new Vao();
		
		textVbo = new Rbo(GL_ARRAY_BUFFER);
		
		 /* Create FloatBuffer */
		//We need to flush the render when we exceed this buffers capacity
        textVertices = BufferUtils.createFloatBuffer(4096*10);

        textVao.bind();
        
        /* Upload null data to allocate storage for the VBO */
        long size = textVertices.capacity() * Float.BYTES;
        textVbo.bind();
        textVbo.uploadData(size, GL_DYNAMIC_DRAW);
        
        /* Initialize variables */
        numTextVertices = 0;
        
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
        		
	}
	
	public void input(Controlls controlls) {
		cam.input(controlls);
		if(controlls.getKeyboad().activeKeyEvent()) {
			if(controlls.getKeyboad().isKeyDown(GLFW_KEY_F)) {
				if(wireframe) {
					wireframe = false;
				} else {
					wireframe = true;
				}			
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
		glEnable(GL_CULL_FACE);
		
		if(wireframe) {
			glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
		} else {
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
		}
		
		FloatBuffer fb = cam.getMVMatrix();
		
		num3DVertices=0;
		
		renderObjects.values().stream().forEach(rboMap -> {
			Vao vao = (Vao) rboMap.get("VAO");
			int entities = vao.getCount();
			
			vao.bind();
			
            shaderProgram.bind();

            glUniformMatrix4fv(matLocation, false, fb);
            glDrawElements(GL_TRIANGLES, (36*entities)+1, GL_UNSIGNED_INT, 1);
            
            shaderProgram.unbind();
            vao.unbind();
            
           
            num3DVertices+=(36*entities);            
			
		});
		
		Window.queScreenMessage("DebugOverlay", new ScreenMessage("Total Rendered Cubes: "+num3DVertices/36));
		Window.queScreenMessage("DebugOverlay", new ScreenMessage("Verts: "+num3DVertices));
		Window.queScreenMessage("DebugOverlay", new ScreenMessage("Total Depicted Cubes: "+totalDepictedEntities));
		totalDepictedEntities=0;
	}
	
	private void renderText() {
		
		if (numTextVertices > 0) {
			glDisable(GL_CULL_FACE);
			glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
			textVertices.flip();
            textVao.bind();
            textShaderProgram.bind();
            /* Upload the new vertex data */
            textVbo.bind();
            textVbo.uploadSubData(0, textVertices);
            /* Draw batch */
            glDrawArrays(GL_TRIANGLES, 0, numTextVertices);
            /* Clear vertex data for next batch */
            numTextVertices = 0;
        }
		
		textVertices.clear();
	}
	
	public Camera getCamera() {
		return this.cam;
	}
	
	public void dispose() {
		 
		renderObjects.values().stream().forEach(rbo -> {
			((Vao) rbo.get("VAO")).unbind();
			((Rbo) rbo.get("VBO")).unbind();
			((Rbo) rbo.get("EBO")).unbind();
		});
		
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
	
        numTextVertices += 6;
	}
	
	public void print(float x, float y, Font font, String text) {
		print(x, y, font, text, new Vector3f(1f,1f,1f));
	}

	public void print(float x, float y, Font font, String text, Vector3f color) {
		xb.put(0, x);
		yb.put(0, y);

		font.getChardata().position(font.getFontTexture().getId() * 128 * STBTTPackedchar.SIZEOF);

		font.getFontTexture().bind();

		for ( int i = 0; i < text.length(); i++ ) {
			stbtt_GetPackedQuad(font.getChardata(), font.getBitMapW(), font.getBitMapH(), text.charAt(i), xb, yb, q.buffer(), 0);
			drawBoxTC(
				q.getX0(), q.getY0(), q.getX1(), q.getY1(),
				q.getS0(), q.getT0(), q.getS1(), q.getT1(),
				color
			);
		}
	}
	
	public <T> NonBlockingFuture<T> call(Callable<T> callable) {
		return executor.submitNonBlocking(callable);
	}
	
}
