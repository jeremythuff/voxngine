package game.world.terrain;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import game.world.WorldObject;
import voxngine.graphics.RenderEngine;
import voxngine.io.Controlls;
import voxngine.io.ScreenMessage;
import voxngine.io.Window;

public class Cube implements WorldObject {
	
	ExecutorService executor = Executors.newSingleThreadExecutor();
	
	private RenderEngine renderer;
	private int renderObjectId;
	
	private FloatBuffer vertBuffer;
	private IntBuffer indecesBuffer;
	
	private List<Vector3f> culledCoords = new ArrayList<Vector3f>();
	
	private CubeGeometry cubeGeo;
	
	private int xCubes;
	private int yCubes;
	private int zCubes;
	
	private int xOrigin;
	private int yOrigin;
	private int zOrigin;
	
	private int totalCubes;
	
	boolean shrinkingX = true;
	boolean shrinkingY = true;
	boolean shrinkingZ = true;
	
	boolean buildingBuffers = false;
	
	float tick = 0;
	
	public Cube(int xCubes, int yCubes, int zCubes, 
				int xOrigin, int yOrigin, int zOrigin) {
		
		this.xCubes = xCubes;
		this.yCubes = yCubes;
		this.zCubes = zCubes;
		
		this.xOrigin = xOrigin;
		this.yOrigin = yOrigin;
		this.zOrigin = zOrigin;
		
		totalCubes = xCubes*yCubes*zCubes;
	}
			
	@Override
	public void init(RenderEngine renderer) {
		
		cubeGeo = new CubeGeometry();
        int geoLength = cubeGeo.getVertices(new Vector3f(0,0,0)).length;
        
        vertBuffer = BufferUtils.createFloatBuffer(totalCubes*geoLength);
        indecesBuffer = BufferUtils.createIntBuffer(totalCubes*36);
        
        buildBuffers();
        
        renderObjectId = renderer.registerRenderObject(totalCubes, vertBuffer, indecesBuffer);
        this.renderer = renderer;
	}
		
	@Override
	public void input(Controlls controlls) {
	}

	@Override
	public void update(float delta) {
		
		if(!buildingBuffers)  {
			
			buildingBuffers = true;
			
			Future<?> f = executor.submit(new Runnable() {
				@Override
				public void run() {
					buildBuffers();
				}
			});
			
	        
	        
	        //if(f.isDone()) {
	        	System.out.println("Updating");
	        	renderer.updateRenderObject(renderObjectId, totalCubes, vertBuffer, indecesBuffer);
	        //}
		}
	         
	}
 
	@Override
	public void render(RenderEngine renderer) {
		Window.queScreenMessage("DebugOverlay", new ScreenMessage("capacity: "+vertBuffer.capacity()));
		Window.queScreenMessage("DebugOverlay", new ScreenMessage("Building: "+buildingBuffers));
		Window.queScreenMessage("DebugOverlay", new ScreenMessage("Total Depicted Cubes: "+totalCubes));
		Window.queScreenMessage("DebugOverlay", new ScreenMessage("Total Rendered Cubes: "+(totalCubes-culledCoords.size())));

	}

	@Override
	public void dispose() {
		
	}
	
	private boolean buildBuffers() {
		
		System.out.println("building");
		
		
		
		int geoLength = cubeGeo.getVertices(new Vector3f(0,0,0)).length;
		
    	int updated = (xCubes*yCubes*zCubes)-(culledCoords.size()/2);
    	
		vertBuffer = BufferUtils.createFloatBuffer(updated*geoLength);
        indecesBuffer = BufferUtils.createIntBuffer(updated*36);
		
		int index = 0;
        for(float x=0 ; x < (float)xCubes ; x++) {
        	for (float y=0 ; y < (float)yCubes ; y++) {
        		for (float z=0; z < (float)zCubes ; z++) {
        			
        			Vector3f coords = new Vector3f((x-xOrigin),(y-yOrigin),(z-zOrigin));
        			
        			if(!culledCoords.contains(coords)) {
        				float[] vertices = cubeGeo.getVertices(coords);
        				vertBuffer.put(vertices);
                		
            	        int[] indeces = cubeGeo.getIndices(index);
            	        indecesBuffer.put(indeces);
            	        index++;
                	} 
        		}
        	}
        }
		
        culledCoords = new ArrayList<Vector3f>();
		
		Map<Vector3f, Integer> cullables = new HashMap<Vector3f, Integer>();
		for(float x=0 ; x < (float)xCubes ; x++) {
        	for (float y=0 ; y < (float)yCubes ; y++) {
        		for (float z=0; z < (float)zCubes ; z++) {
        			
        			Vector3f candidates[] = {
        					new Vector3f((x-xOrigin),(y-yOrigin)-1,(z-zOrigin)),
        					new Vector3f((x-xOrigin)+1,(y-yOrigin)-1,(z-zOrigin)+1),
        					new Vector3f((x-xOrigin)+1,(y-yOrigin)-1,(z-zOrigin)-1),
        					new Vector3f((x-xOrigin)-1,(y-yOrigin)-1,(z-zOrigin)+1),
        					new Vector3f((x-xOrigin)-1,(y-yOrigin)-1,(z-zOrigin)-1),
        					new Vector3f((x-xOrigin)-1,(y-yOrigin)-1,(z-zOrigin)),
        					new Vector3f((x-xOrigin)+1,(y-yOrigin)-1,(z-zOrigin)),
        					new Vector3f((x-xOrigin),(y-yOrigin)-1,(z-zOrigin)-1),
        					new Vector3f((x-xOrigin),(y-yOrigin)-1,(z-zOrigin)+1)
        			};
        			
        			for(Vector3f cullable : candidates) {
        				
        				if(cullables.get(cullable) == null) {
        					cullables.put(cullable, 1);
        				} else {
        					int curentValue = cullables.get(cullable);
        					if(curentValue > 7) {
        						culledCoords.add(cullable);
        					} else {
        						cullables.replace(cullable, curentValue+=1);
        					}
        				}
        			}
        			
        		}
        	}
        }
				
		
        
        vertBuffer.flip();        
        indecesBuffer.flip();
        
		buildingBuffers = false;

        return true;
        
	}

}
