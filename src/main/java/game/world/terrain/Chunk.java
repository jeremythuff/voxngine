package game.world.terrain;

import java.lang.ref.SoftReference;
import java.lang.ref.WeakReference;

import org.joml.Vector3f;

import game.world.WorldObject;
import voxngine.graphics.Mesh;
import voxngine.graphics.RenderEngine;
import voxngine.io.Controlls;
import voxngine.threading.FutureCallback;
import voxngine.threading.NonBlockingFuture;

public class Chunk implements WorldObject {
	
	private RenderEngine renderer;
	private int registeredMeshId;
    private Mesh mesh;
		
	private VoxelGeometry voxGeo;
	private Vector3f startCoords;
	private SoftReference<byte[]> voxelMap;
	
	private int geoLength;
	
    private boolean rebuildEvent = false ;
    private boolean lastActive = false;
    private boolean active = false;
    private boolean currentlyBuilding = false;
    
    private ChunkMaker chunkMaker;
	protected boolean finishedBuidling;
	protected boolean initDone;
		
	public Chunk(Vector3f startCoords, byte[] voxelMap) {	
		this.voxelMap = new SoftReference<byte[]>(voxelMap);
		this.voxGeo = new VoxelGeometry();
		this.startCoords = startCoords;
        this.geoLength = voxGeo.getVertices(new int[]{0,0,0,0}, "front").length*6;	
        this.mesh = new Mesh();
	}
			
	@Override
	public void init(RenderEngine renderer) {
		     
        mesh.setVertBuffer((int) (voxelMap.get().length*geoLength));
		mesh.setIndecesBuffer((int) (voxelMap.get().length*36));
				
		mesh.setEntityCount((int) (voxelMap.get().length));
        
        chunkMaker = new ChunkMaker(mesh, voxelMap.get(), startCoords, rebuildEvent);
		chunkMaker.setActiveChunk(active);
		
		NonBlockingFuture<Mesh> fMesh = renderer.call(chunkMaker);
		
		fMesh.setCallback(new FutureCallback<Mesh>() {
			
			@Override
			public void onSuccess(Mesh newMesh) {
				mesh = newMesh;
				initDone = true;
			}
			
			@Override
            public void onFailure(Throwable e) {
                System.out.println("Error on non blocking callable"+ e.getMessage());
            }
		});
        
        this.renderer = renderer;
        
	}
		
	@Override
	public void input(Controlls controlls) {
//		if(active) {
//			if(controlls.getKeyboad().activeKeyEvent()) {
//				if(controlls.getKeyboad().isKeyDown(GLFW_KEY_UP)) {
//					voxCount.y++;
//					rebuildEvent = true;
//				}
//				
//				if(controlls.getKeyboad().isKeyDown(GLFW_KEY_DOWN)) {
//					voxCount.y--;
//					rebuildEvent = true;
//				}
//			}
//		}
	}

	@Override
	public void update(float delta) {
		
		if(initDone) {
			initDone = false;
			registeredMeshId = renderer.registerMesh(mesh);
			System.out.println("Chunk "+ this.registeredMeshId +" is initialized...");
	        rebuildEvent = true;
		}
		
		int updatedCount = (int) (voxelMap.get().length);
		renderer.updateDepictedEntityCount(updatedCount);
				
		if(!currentlyBuilding && ((rebuildEvent || lastActive) && (active || lastActive)))  {
			
			currentlyBuilding = true;
			lastActive = false;		
					
			mesh.updateVertBuffer();
			mesh.updateIndecesBuffer();
				       			
			chunkMaker.setMesh(mesh);
			chunkMaker.setRebuildEvent(rebuildEvent);
			chunkMaker.setActiveChunk(active);
			
			NonBlockingFuture<Mesh> fMesh = renderer.call(chunkMaker);
			
			fMesh.setCallback(new FutureCallback<Mesh>() {
				
				@Override
				public void onSuccess(Mesh newMesh) {
					finishedBuidling = true;
					mesh = newMesh;
				}
				
				@Override
	            public void onFailure(Throwable e) {
	                System.out.println("Error on non blocking callable"+ e.getMessage());
	            }
			});
			
			rebuildEvent = false;
			
		}
		
		if(finishedBuidling) {
			finishedBuidling = false;
			currentlyBuilding = false;
			renderer.updateMesh(registeredMeshId, mesh);
		}
	}
	
	public boolean getActive() {
		return this.active;
	}
	
	public void setActive(boolean active) {
		this.active = active;
	}
 
	@Override
	public void render(RenderEngine renderer) {
		
	}

	@Override
	public void dispose() {}

	public void setLastActive(boolean lastActive) {
		this.lastActive = lastActive;
	}
	

}


