package voxngine.camera;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_A;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_D;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_S;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_SPACE;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_W;
import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.GLFW_RELEASE;

import java.nio.FloatBuffer;

import org.joml.FrustumCuller;
import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.camera.ArcBallCamera;
import org.lwjgl.BufferUtils;

import voxngine.io.Controlls;
import voxngine.io.ScreenMessage;
import voxngine.io.Window;

public class TPCamera implements Camera {
	
	ArcBallCamera cam = new ArcBallCamera();
	private Matrix4f viewProjMatrix = new Matrix4f();
	private FloatBuffer fb = BufferUtils.createFloatBuffer(16);
	private FrustumCuller fc = new FrustumCuller();
	
	private double displayAlpha;
	private double displayBeta;
	private double lastDisplayAlpha;
	private double lastDisplayBeta;
	
	long window;
	int x, y;
	
	float xMove=0.0f;
	float yMove=0.0f;
	float zMove=0.0f;
	
	float zoom = 200;
	int mouseX, mouseY;
	boolean down;
	
	@Override
	public void init() {
		cam.setAlpha((float) Math.toRadians(-45));
		cam.setBeta((float) Math.toRadians(45));
		cam.zoom(zoom); 
		cam.update(100);
	}
	
	@Override
	public void input(Controlls controlls) {
		if(controlls.getKeyboad().activeKeyEvent()) {
			
			
			if(controlls.getKeyboad().isKeyDown(GLFW_KEY_SPACE)) {
				if(yMove < 0.1f) {
					yMove = 1f;
				}
			}
			
			if(controlls.getKeyboad().isKeyDown(GLFW_KEY_W)) { 
				zMove = -1f;
				xMove = -1f;
			} else if(controlls.getKeyboad().isKeyDown(GLFW_KEY_A)) { 
				zMove = 1f;
				xMove = -1f;
			} else if(controlls.getKeyboad().isKeyDown(GLFW_KEY_S)) { 
				zMove = 1f;
				xMove = 1f;
			} else if(controlls.getKeyboad().isKeyDown(GLFW_KEY_D)) { 
				zMove = -1f;
				xMove = 1f;
			} else {
				xMove = 0f;
				zMove = 0f;
			}
			
			
		}
		
		if(controlls.getMouse().activeMoveEvent()) {
			x = (int) controlls.getMouse().getPos().x - Window.WIDTH / 2;
			y = Window.HEIGHT / 2 - (int) controlls.getMouse().getPos().y;
		}
		
		if(controlls.getMouse().activeClickEvent()) {
			if (controlls.getMouse().actionIs(GLFW_PRESS)) {
				down = true;
				mouseX = x;
				mouseY = y;
			} else if (controlls.getMouse().actionIs(GLFW_RELEASE)) {
				down = false;
			}
		}
		
		if(controlls.getMouse().activeScrollEvent()) {
			if (controlls.getMouse().getScrollDelta().y > 0) {
				zoom /= 1.05f;
			} else {
				
				if(zoom < 250) {
					zoom *= 1.05f;
				} else {
					zoom /= 1.05f;
				}
			    
			
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
	          
	          if(cam.getBeta() < 0.20943951023932117) {
		          cam.setBeta(0.20943951023932117 + 0.0001);
		      }
	      }
	      
	      
	      System.out.println(cam.getBeta());
	      
	     
	      
	      cam.zoom(zoom);
	      cam.update((float) (delta*2));
	      
	      if(yMove > 0.0f) {
	    	  yMove -= 0.1f;
	      }
	      
	      Vector3f currentPos = cam.centerMover.current;
	      cam.center(currentPos.x+xMove, currentPos.y=yMove, currentPos.z+zMove);
			
		  cam.viewMatrix(viewProjMatrix).setPerspective((float) Math.atan((32.5 * Window.HEIGHT / 1200) / 60.0),
		          (float) Window.WIDTH / Window.HEIGHT, 0.25f, 1000.0f);
		  
		  fc = viewProjMatrix.getFrustum(fc);
		  		  	
		  displayAlpha = Math.round(cam.getAlpha()*100.0);
			
		  if(displayAlpha > 360d) displayAlpha = displayAlpha + Math.round(cam.getAlpha()*100.0) - 360;
		  if(displayAlpha < 0d) displayAlpha = displayAlpha + Math.round(cam.getAlpha()*100.0) + 360;
			
		  displayBeta = Math.round(cam.getBeta()*100.0);
			
		  if(displayBeta > 360d) displayBeta = displayBeta + Math.round(cam.getBeta()*100.0) - 360;
		  if(displayBeta < 0d) displayBeta = displayBeta + Math.round(cam.getBeta()*100.0) + 360;
		  	
		  	
		  Window.queScreenMessage("DebugOverlay", new ScreenMessage("CAMERA_DATA", "Zoom: " + zoom));
		  Window.queScreenMessage("DebugOverlay", new ScreenMessage("CAMERA_DATA", "Alpha: " + displayAlpha));
		  Window.queScreenMessage("DebugOverlay", new ScreenMessage("CAMERA_DATA", "Beta: " + displayBeta));
	}
	
	@Override
	public FloatBuffer getMVMatrix() {
		return cam.viewMatrix(viewProjMatrix).get(fb);
	}
	
	@Override
	public boolean frustumCulled(Vector3f point) {		
		return fc.isPointInsideFrustum(point);
	}

}
