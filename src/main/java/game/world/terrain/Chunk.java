package game.world.terrain;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

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

public class Chunk implements WorldObject {
		
	private RenderEngine renderer;
	private int renderObjectId;
		
	private VoxelGeometry cubeGeo;
	
	private int xCubes;
	private int yCubes;
	private int zCubes;
	
	private int xOrigin;
	private int yOrigin;
	private int zOrigin;
	
	int geoLength;
	
	boolean buildingBuffers = false;
	boolean isDone = false;
    private boolean rebuildEvent;
    
    private Mesh mesh;

	private ChunkMaker chunkMaker;
		
	public Chunk(int xCubes, int yCubes, int zCubes, 
				int xOrigin, int yOrigin, int zOrigin) {
		
		this.xCubes = xCubes;
		this.yCubes = yCubes;
		this.zCubes = zCubes;
		
		this.xOrigin = xOrigin;
		this.yOrigin = yOrigin;
		this.zOrigin = zOrigin;
		
		cubeGeo = new VoxelGeometry();
        geoLength = cubeGeo.getVertices(new Vector3f(0,0,0)).length;
        
		chunkMaker = new ChunkMaker();
        mesh = new Mesh();

	}
			
	@Override
	public void init(RenderEngine renderer) {
		
		ExecutorService executor = Executors.newCachedThreadPool();
     
        mesh.setVertBuffer(BufferUtils.createFloatBuffer(xCubes*yCubes*zCubes*geoLength));
		mesh.setIndecesBuffer(BufferUtils.createIntBuffer(xCubes*yCubes*zCubes*36));
        mesh.setEntityCount(xCubes*yCubes*zCubes);
        
		buildingBuffers = true;

		chunkMaker.setMesh(mesh);
		
		Future<Mesh> fMesh = executor.submit(chunkMaker);
		executor.shutdown();
		
		try {
			mesh = fMesh.get();
			
			if(mesh != null) {
		        renderObjectId = renderer.registerRenderObject(mesh);
			} 
		} catch (Exception e) {
			System.out.println("Exception returning from callable!");
			e.printStackTrace();
		} finally  {
			fMesh.cancel(true);
		}
        
        this.renderer = renderer;
        rebuildEvent = true;
        
		System.out.println("Chunk "+ this.renderObjectId +" is initialized...");

        
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
			
			buildingBuffers = true;
			
			ExecutorService executor = Executors.newCachedThreadPool();//.newFixedThreadPool(10);
			
			int updatedCount = (xCubes*yCubes*zCubes);
			
			if(mesh.getVertBuffer() != null) BufferUtils.zeroBuffer(mesh.getVertBuffer());
			if(mesh.getVertBuffer() != null) BufferUtils.zeroBuffer(mesh.getIndecesBuffer());
						
			mesh.setVertBuffer(BufferUtils.createFloatBuffer(updatedCount*geoLength));
			mesh.setIndecesBuffer(BufferUtils.createIntBuffer(updatedCount*36));
			mesh.setEntityCount(xCubes*yCubes*zCubes);
	        
			buildingBuffers = true;
			chunkMaker.setMesh(mesh);
			
			Future<Mesh> fMesh = executor.submit(chunkMaker);
			executor.shutdown();
			
			try {
				mesh = fMesh.get();
				if(mesh != null) {
		        	renderer.updateRenderObject(renderObjectId, mesh);
				} 
			} catch (Exception e) {
				System.out.println("Exception returning from callable!");
				e.printStackTrace();
			} finally  {
				fMesh.cancel(true);
			}
	     
		}
	         
	}
 
	@Override
	public void render(RenderEngine renderer) {}

	@Override
	public void dispose() {}
	
	class ChunkMaker implements Callable<Mesh> {
		
		private Mesh mesh;
		
		@Override
		public Mesh call() throws Exception {
			Map<String, Integer> cullables = new HashMap<String, Integer>();
			Vector3f vector = new Vector3f();
			
			HashSet<String> workingCulledCoords = mesh.getCulledCoords();
			if(rebuildEvent) {
				mesh.setCulledCoords(new HashSet<String>());
			}
					
	        int index = 0;
	        for(float x=0 ; x < xCubes ; x++) {

	        	for (float y=0 ; y < yCubes ; y++) {

	        		for (float z=0; z < zCubes ; z++) {
	        			
	        			vector.set((x-xOrigin),(y-yOrigin),(z-zOrigin));
	        			
	        			if(!workingCulledCoords.contains(vector.x+"-"+vector.y+"-"+vector.z)) {
	        				float[] vertices = cubeGeo.getVertices(vector);
	        				mesh.getVertBuffer().put(vertices);
	                		
	            	        int[] indeces = cubeGeo.getIndices(index);
	            	        mesh.getIndecesBuffer().put(indeces);
	            	        index++;
	                	} else if(!rebuildEvent) {
	                		continue;
	                	}
	        			
	        			cullTest(cullables, vector.set((x-xOrigin),(y-yOrigin)-2,(z-zOrigin)));        			
	        			cullTest(cullables, vector.set((x-xOrigin)+1,(y-yOrigin)-2,(z-zOrigin)+1));
	        			cullTest(cullables, vector.set((x-xOrigin)+1,(y-yOrigin)-2,(z-zOrigin)-1));
	        			
	        			cullTest(cullables, vector.set((x-xOrigin)-1,(y-yOrigin)-2,(z-zOrigin)+1));
	        			cullTest(cullables, vector.set((x-xOrigin)-1,(y-yOrigin)-2,(z-zOrigin)-1));
	        			cullTest(cullables, vector.set((x-xOrigin)-1,(y-yOrigin)-2,(z-zOrigin)));
	        			
	        			cullTest(cullables, vector.set((x-xOrigin)+1,(y-yOrigin)-2,(z-zOrigin)));
	        			cullTest(cullables, vector.set((x-xOrigin),(y-yOrigin)-2,(z-zOrigin)-1));
	        			cullTest(cullables, vector.set((x-xOrigin),(y-yOrigin)-2,(z-zOrigin)+1));
	        			
	        		}
	        	}
	        }
	        
	        vector = null;
	        cullables = null;
	        
	        mesh.getVertBuffer().flip();        
	        mesh.getIndecesBuffer().flip();
//	        			        
	        buildingBuffers = false;        
	        if(rebuildEvent) rebuildEvent=false;
	        
	        return mesh;
		}
		
		private void setMesh(Mesh mesh) {
			this.mesh = mesh;
		}

	}
	
	private Map<String, Integer> cullTest(Map<String, Integer> cullables, Vector3f vector) {
		
		String coords = vector.x+"-"+vector.y+"-"+vector.z;
		
		if(cullables.get(coords) == null) {
			cullables.put(coords, 1);
		} else {
			int curentValue = cullables.get(coords);			
			if(curentValue > 7) {
				mesh.getCulledCoords().add(coords);
			} else {
				cullables.replace(coords, curentValue+=1);
			}
		}
		
		return cullables;
		
	}

}


