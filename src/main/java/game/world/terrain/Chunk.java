package game.world.terrain;

import static org.lwjgl.glfw.GLFW.GLFW_KEY_DOWN;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_UP;

import java.util.concurrent.Future;

import org.joml.Vector3f;

import game.world.WorldObject;
import voxngine.graphics.Mesh;
import voxngine.graphics.RenderEngine;
import voxngine.io.Controlls;
import voxngine.utils.FutureCallback;
import voxngine.utils.NonBlockingFuture;

public class Chunk implements WorldObject {
	
	private RenderEngine renderer;
	private int registeredMeshId;
    private Mesh mesh;
		
	private VoxelGeometry voxGeo;
	
	private Vector3f voxCount;
	private Vector3f positionOffset;
	
	int geoLength;
	
    private boolean rebuildEvent = false ;
    private boolean lastActive = false;
    private boolean active = false;
    private boolean currentlyBuilding = false;
    
    private ChunkMaker chunkMaker;
	protected boolean finishedBuidling;
	protected boolean initDone;
		
	public Chunk(int xVox, int yVox, int zVox, 
				int xOrigin, int yOrigin, int zOrigin) {
		
		this.voxCount = new Vector3f(xVox, yVox, zVox);
		this.positionOffset = new Vector3f(xOrigin, yOrigin, zOrigin);
		
		voxGeo = new VoxelGeometry();
        geoLength = voxGeo.getVertices(new Vector3f(0,0,0), "front").length*6;
       		
        mesh = new Mesh();

	}
			
	@Override
	public void init(RenderEngine renderer) {
		     
        mesh.setVertBuffer((int) (voxCount.x*voxCount.y*voxCount.z*geoLength));
		mesh.setIndecesBuffer((int) (voxCount.x*voxCount.y*voxCount.z*36));
		mesh.setEntityCount((int) (voxCount.x*voxCount.y*voxCount.z));
        
        chunkMaker = new ChunkMaker(mesh, voxCount, positionOffset, rebuildEvent);
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
                System.out.println(e.getMessage());
            }
		});
        
        this.renderer = renderer;
        
	}
		
	@Override
	public void input(Controlls controlls) {
		if(active) {
			if(controlls.getKeyboad().activeKeyEvent()) {
				if(controlls.getKeyboad().isKeyDown(GLFW_KEY_UP)) {
					voxCount.y++;
					rebuildEvent = true;
				}
				
				if(controlls.getKeyboad().isKeyDown(GLFW_KEY_DOWN)) {
					voxCount.y--;
					rebuildEvent = true;
				}
			}
		}
	}

	@Override
	public void update(float delta) {
		
		if(initDone) {
			initDone = false;
			registeredMeshId = renderer.registerMesh(mesh);
			System.out.println("Chunk "+ this.registeredMeshId +" is initialized...");
	        rebuildEvent = true;
		}
		
		int updatedCount = (int) (voxCount.x*voxCount.y*voxCount.z);
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
	                System.out.println(e.getMessage());
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


