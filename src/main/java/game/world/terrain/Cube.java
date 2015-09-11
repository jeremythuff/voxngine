package game.world.terrain;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.joml.Vector3f;
import org.lwjgl.BufferUtils;

import game.world.WorldObject;
import voxngine.graphics.Mesh;
import voxngine.graphics.RenderEngine;
import voxngine.io.Controlls;

public class Cube implements WorldObject {
	
	ExecutorService executor = Executors.newFixedThreadPool(1);
	
	private RenderEngine renderer;
	private int renderObjectId;
	
	private HashSet<String> culledCoords = new HashSet<String>();
	
	private CubeGeometry cubeGeo;
	
	private int xCubes;
	private int yCubes;
	private int zCubes;
	
	private int xOrigin;
	private int yOrigin;
	private int zOrigin;
	
	int geoLength;
	
	private int totalCubes;

	boolean buildingBuffers = false;
	boolean isDone = false;
    private boolean rebuildEvent;
    
    private Mesh mesh;
		
	public Cube(int xCubes, int yCubes, int zCubes, 
				int xOrigin, int yOrigin, int zOrigin) {
		
		this.xCubes = xCubes;
		this.yCubes = yCubes;
		this.zCubes = zCubes;
		
		this.xOrigin = xOrigin;
		this.yOrigin = yOrigin;
		this.zOrigin = zOrigin;
		
		totalCubes = xCubes*yCubes*zCubes;
		cubeGeo = new CubeGeometry();
        geoLength = cubeGeo.getVertices(new Vector3f(0,0,0)).length;
        
        mesh = new Mesh();

	}
			
	@Override
	public void init(RenderEngine renderer) {
     
        mesh.setVertBuffer(BufferUtils.createFloatBuffer(totalCubes*geoLength));
		mesh.setIndecesBuffer(BufferUtils.createIntBuffer(totalCubes*36));
        
		buildingBuffers = true;
		Future<Mesh> fMesh = executor.submit(new CubeMaker(mesh));
		
		try {
			mesh = fMesh.get();
			if(mesh != null) {
		        renderObjectId = renderer.registerRenderObject(totalCubes, mesh.getVertBuffer(), mesh.getIndecesBuffer());
			} 
		} catch (Exception e) {
			System.out.println("Exception returning from callable!");
			e.printStackTrace();
		}
        
        this.renderer = renderer;
        rebuildEvent = true;
        
	}
		
	@Override
	public void input(Controlls controlls) {
		if(controlls.getKeyboad().activeKeyEvent()) {
			if(controlls.getKeyboad().isKeyDown(GLFW_KEY_UP)) {
				yCubes++;
				rebuildEvent = true;
			}
			
			if(controlls.getKeyboad().isKeyDown(GLFW_KEY_DOWN)) {
				yCubes--;
				rebuildEvent = true;
			}
		}
	}

	@Override
	public void update(float delta) {
		
		if(!buildingBuffers && rebuildEvent)  {
			
			int updated = (xCubes*yCubes*zCubes) - (culledCoords.size()/2);
			totalCubes = updated;
			
			
			BufferUtils.zeroBuffer(mesh.getVertBuffer());
			BufferUtils.zeroBuffer(mesh.getIndecesBuffer());
			
			mesh.setVertBuffer(BufferUtils.createFloatBuffer(updated*geoLength));
			mesh.setIndecesBuffer(BufferUtils.createIntBuffer(updated*36));
	        
			buildingBuffers = true;
			Future<Mesh> fMesh = executor.submit(new CubeMaker(mesh));
			
			try {
				mesh = fMesh.get();
				if(mesh != null) {
		        	renderer.updateRenderObject(renderObjectId, totalCubes, mesh.getVertBuffer(), mesh.getIndecesBuffer());
				} 
			} catch (Exception e) {
				System.out.println("Exception returning from callable!");
				e.printStackTrace();
			}
	     
		}
	         
	}
 
	@Override
	public void render(RenderEngine renderer) {}

	@Override
	public void dispose() {
		
	}
	
	class CubeMaker implements Callable<Mesh> {
		
		private Mesh mesh;
		
		public CubeMaker(Mesh buffers) {
			this.mesh = buffers;
		}

		@Override
		public Mesh call() throws Exception {
			Map<String, Integer> cullables = new HashMap<String, Integer>();
			Vector3f vector = new Vector3f();
			
			HashSet<String> workingCulledCoords = culledCoords;
			culledCoords = new HashSet<String>();
					
	        int index = 0;
	        for(float x=0 ; x < (float)xCubes ; x++) {

	        	for (float y=0 ; y < (float)yCubes ; y++) {

	        		for (float z=0; z < (float)zCubes ; z++) {

	        			vector.set((x-xOrigin),(y-yOrigin),(z-zOrigin));

	        			cullables = cullTest(cullables, vector.set((x-xOrigin),(y-yOrigin)-1,(z-zOrigin)));        			
	        			cullables = cullTest(cullables, vector.set((x-xOrigin)+1,(y-yOrigin)-1,(z-zOrigin)+1));
	        			cullables = cullTest(cullables, vector.set((x-xOrigin)+1,(y-yOrigin)-1,(z-zOrigin)-1));
	        			
	        			cullables = cullTest(cullables, vector.set((x-xOrigin)-1,(y-yOrigin)-1,(z-zOrigin)+1));
	        			cullables = cullTest(cullables, vector.set((x-xOrigin)-1,(y-yOrigin)-1,(z-zOrigin)-1));
	        			cullables = cullTest(cullables, vector.set((x-xOrigin)-1,(y-yOrigin)-1,(z-zOrigin)));
	        			
	        			cullables = cullTest(cullables, vector.set((x-xOrigin)+1,(y-yOrigin)-1,(z-zOrigin)));
	        			cullables = cullTest(cullables, vector.set((x-xOrigin),(y-yOrigin)-1,(z-zOrigin)-1));
	        			cullables = cullTest(cullables, vector.set((x-xOrigin),(y-yOrigin)-1,(z-zOrigin)+1));
	        			
	        			if(!workingCulledCoords.contains(vector.x+"-"+vector.y+"-"+vector.z)) {
	        				float[] vertices = cubeGeo.getVertices(vector);
	        				mesh.getVertBuffer().put(vertices);
	                		
	            	        int[] indeces = cubeGeo.getIndices(index);
	            	        mesh.getIndecesBuffer().put(indeces);
	            	        index++;
	                	} 
	        			
	        		}
	        	}
	        }
					
	        mesh.getVertBuffer().flip();        
	        mesh.getIndecesBuffer().flip();
			        
	        buildingBuffers = false;
	                
	        if(rebuildEvent) rebuildEvent=false;
	        
	        return mesh;
		}

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


