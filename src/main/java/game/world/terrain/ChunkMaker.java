package game.world.terrain;

import java.util.concurrent.Callable;

import org.joml.Vector3f;
import org.joml.Vector4f;

import voxngine.graphics.Mesh;

class ChunkMaker implements Callable<Mesh> {
	
	private Mesh mesh;
	private Vector4f[] voxMap;
	private VoxelGeometry voxGeo;
	
	private boolean rebuildEvent;
	private boolean activeChunk;

	private Vector3f startingCoords;
	
		
	ChunkMaker(Mesh mesh, Vector3f startingCoords, Vector4f[] voxMap, boolean rebuildEvent) {
		this.mesh = mesh;
		this.voxMap = voxMap;
		this.voxGeo = new VoxelGeometry();
		this.startingCoords = startingCoords;
				
		this.rebuildEvent = rebuildEvent;
	}
	
	public void setMesh(Mesh mesh) {
		this.mesh = mesh;
	}
	
	public void setRebuildEvent(boolean rebuildEvent) {
		this.rebuildEvent = rebuildEvent;
	}
	
	public void setActiveChunk(boolean activeChunk) {
		this.activeChunk = activeChunk;
	}
	
	@Override
	public Mesh call() throws Exception {
				
		Vector3f vector = new Vector3f();
				
        int index = 0;
       
        
        for(Vector4f voxel : voxMap) {
        	
        	if(voxel.w==-1) continue;
        	
        	vector.set(voxel.x-startingCoords.x, voxel.y-startingCoords.y, voxel.z-startingCoords.z);
        	
        	int faceCount = 0;
    		
        	if(hiddenFaces(vector, "front")) {
    			mesh.getVertBuffer().put(voxGeo.getVertices(vector, "front"));
    			faceCount++;
    		}
    		
    		if(hiddenFaces(vector, "right")) {
    			mesh.getVertBuffer().put(voxGeo.getVertices(vector, "right"));
    			faceCount++;
    		}
    		
    		if(hiddenFaces(vector, "back")) {
    			mesh.getVertBuffer().put(voxGeo.getVertices(vector, "back"));
    			faceCount++;
    		}
    		
    		if(hiddenFaces(vector, "left")) {
    			mesh.getVertBuffer().put(voxGeo.getVertices(vector, "left"));
    			faceCount++;
    		}
    		
    		if(hiddenFaces(vector, "bottom")) {
    			mesh.getVertBuffer().put(voxGeo.getVertices(vector, "bottom"));
    			faceCount++;
    		}
    		
    		if(hiddenFaces(vector, "top")) {
    			mesh.getVertBuffer().put(voxGeo.getVertices(vector, "top"));
    			faceCount++;
    		}
    		
    		for(int i = 0; i<faceCount ; i++) {
    			mesh.getIndecesBuffer().put(voxGeo.getIndices(index, i));	 
    		}
            	
            index += faceCount*4;
        }
        
        mesh.getVertBuffer().flip();        
        mesh.getIndecesBuffer().flip();
        
        vector = null;
        
        return mesh;
	}
	
	private boolean hiddenFaces(Vector3f vector, String string) {
		return true;
	}
	
}
