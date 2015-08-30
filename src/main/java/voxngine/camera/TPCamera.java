package voxngine.camera;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_F;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;
import static org.lwjgl.opengl.GL11.GL_FILL;
import static org.lwjgl.opengl.GL11.GL_FRONT_AND_BACK;
import static org.lwjgl.opengl.GL11.GL_LINE;
import static org.lwjgl.opengl.GL11.glPolygonMode;

import java.nio.FloatBuffer;

import org.joml.Matrix4f;
import org.joml.camera.ArcBallCamera;
import org.lwjgl.BufferUtils;

import voxngine.io.Keyboard;
import voxngine.io.Mouse;
import voxngine.io.Window;

public class TPCamera extends AbstractCamera {
	
	ArcBallCamera cam = new ArcBallCamera();
	private static Matrix4f viewProjMatrix = new Matrix4f();
	private static FloatBuffer fb = BufferUtils.createFloatBuffer(16);
	
	static boolean wireframe;
	
	long window;
	int x, y;
	float zoom = 200;
	int mouseX, mouseY;
	boolean down;
	
	public void init() {
		cam.setAlpha((float) Math.toRadians(-45));
		cam.setBeta((float) Math.toRadians(15));
		cam.zoom(zoom); 
		cam.update(100);
	}
	
	public void input() {
		if(Keyboard.isKeyDown(GLFW_KEY_F)) {
			if(wireframe) {
				wireframe = false;
				glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);
			} else {
				glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);
				wireframe = true;
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
				zoom /= 1.05f;
			} else {
			    zoom *= 1.05f;
			}
		}
	}
	
	public void update(float delta) {
		 /* Set input values for the camera */
	      if (down) {
	          cam.setAlpha(cam.getAlpha() + Math.toRadians((x - mouseX) * 0.5f));
	          cam.setBeta(cam.getBeta() + Math.toRadians((mouseY - y) * 0.5f));
	          mouseX = x;
	          mouseY = y;
	      }
	      
	      cam.zoom(zoom);
	      cam.update((float) (delta*2));
			
		  cam.viewMatrix(viewProjMatrix).setPerspective((float) Math.atan((32.5 * Window.HEIGHT / 1200) / 60.0),
	              (float) Window.WIDTH / Window.HEIGHT, 0.01f, 1000.0f);
	}
	
	public FloatBuffer getVPMatrix() {
		return cam.viewMatrix(viewProjMatrix).get(fb);
	}

}
