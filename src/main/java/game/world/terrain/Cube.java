package game.world.terrain;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import game.world.WorldObject;
import voxngine.graphics.RenderEngine;
import voxngine.io.Controlls;

public class Cube implements WorldObject {
	
	ExecutorService executor = Executors.newFixedThreadPool(1);
	
	private RenderEngine renderer;
	private int renderObjectId;
	
	private FloatBuffer vertBuffer;
	private IntBuffer indecesBuffer;
	
	private HashSet<String> culledCoords = new HashSet<String>();
	
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
	boolean isDone = false;

	
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
        System.out.println(renderObjectId);
        
	}
		
	@Override
	public void input(Controlls controlls) {
	}

	@Override
	public void update(float delta) {
		
		if(!buildingBuffers)  {
			
			buildingBuffers = true;
			executor.submit(new Runnable() {
				@Override
				public void run() {
					buildBuffers();
					isDone = true;
				}
			});
	        
	        if(isDone) {
	        	renderer.updateRenderObject(renderObjectId, totalCubes, vertBuffer, indecesBuffer);
	        }
		}
	         
	}
 
	@Override
	public void render(RenderEngine renderer) {}

	@Override
	public void dispose() {
		
	}
	
	private void buildBuffers() {
		
		int geoLength = cubeGeo.getVertices(new Vector3f(0,0,0)).length;
		
    	int updated = (xCubes*yCubes*zCubes) - (culledCoords.size()/2);
    	    	
		vertBuffer = BufferUtils.createFloatBuffer(updated*geoLength);
        indecesBuffer = BufferUtils.createIntBuffer(updated*36);

        
		Map<String, Integer> cullables = new HashMap<String, Integer>();
		Vector3f vector = new Vector3f();
		
		HashSet<String> workingCulledCoords = culledCoords;
		culledCoords = new HashSet<String>();
				
        int index = 0;
        for(float x=0 ; x < (float)xCubes ; x++) {

        	for (float y=0 ; y < (float)yCubes ; y++) {

        		for (float z=0; z < (float)zCubes ; z++) {

        			Vector3f coords = new Vector3f((x-xOrigin),(y-yOrigin),(z-zOrigin));
        			
        			if(!workingCulledCoords.contains(coords.x+"-"+coords.y+"-"+coords.z)) {
        				float[] vertices = cubeGeo.getVertices(coords);
        				vertBuffer.put(vertices);
                		
            	        int[] indeces = cubeGeo.getIndices(index);
            	        indecesBuffer.put(indeces);
            	        index++;
                	}
        			
        			cullables = cullTest(cullables, vector.set((x-xOrigin),(y-yOrigin)-1,(z-zOrigin)));        			
        			cullables = cullTest(cullables, vector.set((x-xOrigin)+1,(y-yOrigin)-1,(z-zOrigin)+1));
        			cullables = cullTest(cullables, vector.set((x-xOrigin)+1,(y-yOrigin)-1,(z-zOrigin)-1));
        			
        			cullables = cullTest(cullables, vector.set((x-xOrigin)-1,(y-yOrigin)-1,(z-zOrigin)+1));
        			cullables = cullTest(cullables, vector.set((x-xOrigin)-1,(y-yOrigin)-1,(z-zOrigin)-1));
        			cullables = cullTest(cullables, vector.set((x-xOrigin)-1,(y-yOrigin)-1,(z-zOrigin)));
        			
        			cullables = cullTest(cullables, vector.set((x-xOrigin)+1,(y-yOrigin)-1,(z-zOrigin)));
        			cullables = cullTest(cullables, vector.set((x-xOrigin),(y-yOrigin)-1,(z-zOrigin)-1));
        			cullables = cullTest(cullables, vector.set((x-xOrigin),(y-yOrigin)-1,(z-zOrigin)+1));
        			
        			
        		}
        	}
        }
				
        vertBuffer.flip();        
        indecesBuffer.flip();
		buildingBuffers = false;
		
	}
	
	private Map<String, Integer> cullTest(Map<String, Integer> cullables, Vector3f vector) {
		
		String coords = vector.x+"-"+vector.y+"-"+vector.z;
		
		if(cullables.get(coords) == null) {
			cullables.put(coords, 1);
		} else {
			int curentValue = cullables.get(coords);			
			if(curentValue > 7) {
				culledCoords.add(coords);
			} else {
				cullables.replace(coords, curentValue+=1);
			}
		}
		
		return cullables;
		
	}

}


